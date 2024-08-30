import styles from "./Beat.module.css";
import audio1 from "../../assets/Exist.mp3";
import audio2 from "../../assets/Ảo Giác.mp3";
import audio3 from "../../assets/STAY.mp3";
import audio4 from "../../assets/Trần Duy Hưng.mp3";
import { Link, useNavigate } from "react-router-dom";
import WaveSurfer from 'https://cdn.jsdelivr.net/npm/wavesurfer.js@7/dist/wavesurfer.esm.js'
import { useContext, useEffect, useRef, useState } from "react";
import images from "../../assets/images";
import { Context } from "../../common/ContextProvider";
import axios from "axios";

const Beat = () => {
  const waveformRef = useRef();
  const [isPlaying, setIsPlaying] = useState(-1);
  const wavesurfer = useRef(null);
  const [isWaveSufferReady, setIsWaveSufferReady] = useState(-1);

  const { isForm , turnOnOrOffForm, checkTokenAndRefreshToken, role, checkRole} = useContext(Context);

  const [postedBeats, setPostedBeats] = useState([]);
  const [beatGenres, setBeatGenres] = useState([]);
  const [isLoad, setIsLoad] = useState(true);

  const [total, setTotal] = useState(0);
  const [isSearch, setIsSearch] = useState(false);

  const [genreId, setGenreId] = useState(0);

  const [beatFileName, setBeatFileName] = useState('Beat File');
  
  const [favoriteArray, setFavoriteArray] = useState([]);


  const navigate = useNavigate();

  useEffect(() => {
    setIsLoad(true);
 
    checkRole();

    let array = [];

    const fetchFavorite = async () => {
      if(localStorage.getItem('user') || sessionStorage.getItem('user')) {
        const checkTokenAndRefreshTokenFunction = async () => {
          await checkTokenAndRefreshToken();
        }
  
        const fetchDataFavorite = async () => {
          let accountOwnerId = null;
          if(localStorage.getItem('user')){
            accountOwnerId = JSON.parse(localStorage.getItem('user')).id;
          }
  
          if(sessionStorage.getItem('user')){
            accountOwnerId = JSON.parse(sessionStorage.getItem('user')).id;
          }
  
          await axios.get('http://localhost:8222/api/favorites/favorite-beat/find-by-account-owner-id/'+accountOwnerId, {
            headers: {
              'Authorization': 'Bearer '+ localStorage.getItem('access_token')
            }
          })
          .then(response => {
            setFavoriteArray(response.data);
            array = response.data;
          })
          .catch(error => {
            console.log(error);
          })
        }
  
        await checkTokenAndRefreshTokenFunction();
  
        await fetchDataFavorite();
      }
    }

    fetchFavorite();
    const wait = (ms) => new Promise(resolve => setTimeout(resolve, ms));

    
    const fetchPostedBeats = async () =>{
      await wait(2000);
      await axios.get('http://localhost:8222/api/beats/posted-beat', {
        params: {
          page: Math.floor(postedBeats.length /3)
        }
      })
      .then( async response => {
        setTotal(response.data.total);
        const postedBeatPromise = response.data.postedBeats.map(async postedBeat => {
          try {
            const isFavorite = array.length >0 ? array.some(row => row.postedBeat.id == postedBeat.id): false;
            const favoriteId = array.filter(row => row.postedBeat.id == postedBeat.id)[0];
            return {
                ...postedBeat,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
        });

        const newPostedBeat = await Promise.all(postedBeatPromise);
        setPostedBeats(newPostedBeat);
      })
      .catch(error => {
        console.log(error);
      });
    } 

    const fetchBeatGenres = async () =>{
      await axios.get('http://localhost:8222/api/beats/beat-genre')
      .then(response => {
        setBeatGenres(response.data);
      })
      .catch(error => {
        console.log(error);
      });
    } 

    fetchPostedBeats();
    fetchBeatGenres();
    setIsLoad(false);

  }, []);

  useEffect(() => {
    setBeatFileName('Beat File');
  }, [isForm]);

  const searchBeats = (id) => {
    setIsPlaying(-1);
    setIsWaveSufferReady(-1); 
    setIsLoad(true);
    let beatName = document.getElementById('beat-name').value;
    let beatGenreId = id ? id: 0;

    if(beatName == '' && beatGenreId == 0){
      setIsSearch(false);
    }else {
      setIsSearch(true);
    }

    setGenreId(beatGenreId);

    axios.get('http://localhost:8222/api/beats/posted-beat/find-by-beat-genre-id-and-beat-name',{
      params: {
        "beat-genre-id" : beatGenreId,
        "beat-name": beatName,
        page: 0
      }
    })
    .then(async response => {
      setTotal(response.data.total);
        const postedBeatPromise = response.data.postedBeats.map(async postedBeat => {
          try {
            const isFavorite = favoriteArray.length >0 ? favoriteArray.some(row => row.postedBeat.id == postedBeat.id): false;
            const favoriteId = favoriteArray.filter(row => row.postedBeat.id == postedBeat.id)[0];
            return {
                ...postedBeat,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
        });

        const newPostedBeat = await Promise.all(postedBeatPromise);
        setPostedBeats(newPostedBeat);
        setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  }

  useEffect(() => {
    setIsLoad(true);
    const postedBeat = postedBeats.filter(postedBeat => postedBeat.id === isWaveSufferReady);
    if(waveformRef.current) {
      wavesurfer.current  = WaveSurfer.create({
        container: waveformRef.current,
        waveColor: 'gray',
        progressColor: 'rgb(73, 184, 186)',
        url: 'http://localhost:8222/api/beats/beat/get-beat/'+ postedBeat[0].beat.beatFile,
        height: 60,
        width: "30vw",
        dragToSeek: true,
        hideScrollbar: true,
        normalize: true,
        barGap: 1,
        height: 60,
        barHeight: 20,
        barRadius: 20,
        barWidth: 5,
      });
    }

    setIsLoad(false);

    return () => {
      if (wavesurfer.current) {
        wavesurfer.current.destroy();
      }
    };

  }, [isWaveSufferReady]);

  const playOrPause = async (id) => {
    if(isWaveSufferReady == -1){
      setIsWaveSufferReady(id);
      await new Promise(resolve => setTimeout(resolve, 1));
    }

    if(isWaveSufferReady != id){
      setIsWaveSufferReady(id);
      await new Promise(resolve => setTimeout(resolve, 1));
    }


    if(isPlaying != -1 && isPlaying == id){
      setIsPlaying(-1);
      let playPromise = wavesurfer.current.play();
      if (playPromise !== undefined) {
        playPromise.then(_ => {
          // Automatic playback started!
          // Show playing UI.
          // We can now safely pause video...
          wavesurfer.current.pause();
        })
        .catch(error => {
          // Auto-play was prevented
          // Show paused UI.
        });
      }
    }else {
      setIsPlaying(id);
      let playPromise = wavesurfer.current.play();
      if (playPromise !== undefined) {
        playPromise.then(_ => {
            // Automatic playback started!
            // Show playing UI.
            // We can now safely pause video...
          })
          .catch(error => {
            // Auto-play was prevented
            // Show paused UI.
          });
        }
      }
  }
 
  const clickToGenre = async () => {
    let genres = document.getElementById('genres');
    if(genres.style.opacity == 0  ){
      genres.style.pointerEvents = 'auto';
      genres.style.height = 'auto';
      genres.style.padding = '50px 0 ';
      await new Promise(resolve => setTimeout(resolve, 300));
      genres.style.opacity = '1';
    }else {
      genres.style.opacity = '0';
      await new Promise(resolve => setTimeout(resolve, 300));
      genres.style.pointerEvents = 'none';
      genres.style.height = '0';
      genres.style.padding = '0';
    }
  }

  const handleLoadMore = () => {
    setIsLoad(true);
    if(isSearch== false) {
      axios.get('http://localhost:8222/api/beats/posted-beat', {
        params: {
          page: Math.floor(postedBeats.length /3)
        }
      })
      .then(async response => {
        setTotal(response.data.total);

        const postedBeatPromise = response.data.postedBeats.map(async postedBeat => {
          try {
            const isFavorite = favoriteArray.length >0 ? favoriteArray.some(row => row.postedBeat.id == postedBeat.id): false;
            const favoriteId = favoriteArray.filter(row => row.postedBeat.id == postedBeat.id)[0];
            return {
                ...postedBeat,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
        });

        const newPostedBeat = await Promise.all(postedBeatPromise);
        setPostedBeats(prev => [...prev, ...newPostedBeat]);
        setIsLoad(false);

      })
      .catch(error => {
        console.log(error);
      });
    }else {
      let beatName = document.getElementById('beat-name').value;
  
      axios.get('http://localhost:8222/api/beats/posted-beat/find-by-beat-genre-id-and-beat-name',{
        params: {
          "beat-genre-id" : genreId,
          "beat-name": beatName,
          page: Math.floor(postedBeats.length /3)
        }
      })
      .then(async response => {
        setTotal(response.data.total);

        const postedBeatPromise = response.data.postedBeats.map(async postedBeat => {
          try {
            const isFavorite = favoriteArray.length >0 ? favoriteArray.some(row => row.postedBeat.id == postedBeat.id): false;
            const favoriteId = favoriteArray.filter(row => row.postedBeat.id == postedBeat.id)[0];
            return {
                ...postedBeat,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
        });

        const newPostedBeat = await Promise.all(postedBeatPromise);
        setPostedBeats(prev => [...prev, ...newPostedBeat]);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      });
    }
  }

  const handleSellBeat = async(e) => {
    setIsLoad(true);
    e.preventDefault();

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      turnOnOrOffForm();
      navigate('/sign-in');
      return;
    }

    await checkTokenAndRefreshToken();

    const form = e.target;

    if(form.price.value <=0) {
      alert('Enter price >0')
      setIsLoad(false);
      return;
    }

    const files = form.beatFile.files;
    let beatFile = null;
    if(files.length >0) {
      let formData = new FormData();
      for(let i = 0; i < files.length; i++){
        formData.append('beatFile', files[i]);
      }

      beatFile = await axios.post('http://localhost:8222/api/beats/beat/upload-beat-file', formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          "Authorization": 'Bearer '+localStorage.getItem('access_token')
        }
      })
      .then(response => {
        return response.data;
      })
      .catch(error => {
        console.log(error)
      })
    }else {
      alert('Pleast choose the beat file');
      setIsLoad(false);
      return;
    }

    let sellerId = null;
    if(localStorage.getItem('user')){
      sellerId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      sellerId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    const request = {
      beat: {
        beatGenreId: form.beatGenreId.value,
        beatName: form.beatName.value,
        beatFile: beatFile
      },
      sellerId: sellerId,
      price: form.price.value
    }

    await axios.post('http://localhost:8222/api/beats/posted-beat', request, {
      headers: {
        "Authorization": 'Bearer '+localStorage.getItem('access_token')
      }
    })
    .then( response => {
      window.location.reload();
    })
    .catch(error => {
      console.log(error);
    });

    setIsLoad(false);
  }

  const changeFileName =(e) => {
    const files = e.target.files;
    if(files.length>0) {
      for(let i = 0; i < files.length; i++){
        setBeatFileName(files[i].name)
      }
    }
  }

  const handleAddOrDeleteFavoriteArtist = async (postedBeatId, isFavorite, favoriteId) => {
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }
    
    await checkTokenAndRefreshToken();

    let accountOwnerId = null;
    if(localStorage.getItem('user')){
      accountOwnerId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      accountOwnerId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    if(isFavorite){
      await axios.delete('http://localhost:8222/api/favorites/favorite-beat/'+favoriteId, {
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('access_token')
        }
      })
      .then(() => {
        setPostedBeats(prev => 
          prev.map(postedBeat => 
            postedBeat.id === postedBeatId 
              ? { ...postedBeat, isFavorite: !postedBeat.isFavorite, favoriteId: 0  }
              : postedBeat
          )
        );
        setFavoriteArray(prev => prev.filter(item => item.id !== favoriteId) );
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }else {
      await axios.post('http://localhost:8222/api/favorites/favorite-beat', {
        accountOwnerId: accountOwnerId,
        postedBeatId: postedBeatId
      } ,
      {
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setPostedBeats(prev => 
          prev.map(postedBeat => 
            postedBeat.id === postedBeatId 
              ? { ...postedBeat, isFavorite: !postedBeat.isFavorite, favoriteId: response.data.id }
              : postedBeat
          )
        );
        setFavoriteArray(prev => [
          ...prev, 
          { id: response.data.id, accountOwner: { id: accountOwnerId }, postedBeat: { id: response.data.postedBeat.id } }
        ]);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }

  }

  const handleBuyBeat = async (beatId, sellerId, e) => {
    e.preventDefault();

    const userResponse = window.confirm("After Ok, check your mail and pay !");
    if(userResponse ){
      setIsLoad(true);
  
      if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
        navigate('/sign-in');
        return;
      }

      await checkTokenAndRefreshToken();
  
      let purchaserId = null;
      if(localStorage.getItem('user')){
        purchaserId = JSON.parse(localStorage.getItem('user')).id;
      }
  
      if(sessionStorage.getItem('user')){
        purchaserId = JSON.parse(sessionStorage.getItem('user')).id;
      }
  
      await axios.post('http://localhost:8222/api/purchased-beats/purchased-beat-before-payment', {
        beatId: beatId,
        sellerId: sellerId,
        purchaserId: purchaserId
      }, {
          headers: {
            'Authorization': 'Bearer '+ localStorage.getItem('access_token')
          }
        })
        .then(() => {
          alert('Please check your mail and pay');
          setIsLoad(false);
        })
        .catch(error => {
          console.log(error);
        })
    }

    
  }

  return ( 
    <section className={styles.beat}>
      <div className={styles.container}>
        <div className={styles['search-bar']}>
          <ul>
            <li className={styles.genre}>
              <span onClick={clickToGenre}>
                Genre
              </span>
              <span onClick={clickToGenre}>
                <img src={images.drop} alt="" />
              </span>
            </li>
            <li className={styles.search}>
              <input id="beat-name" type="text" name="name" placeholder="Enter name of beat..." />
              <img onClick={() => searchBeats()} src={images.search} alt="" />
            </li>
          </ul>
        </div>
        <div className={styles['big-genres']}>
          <div id="genres" className={styles.genres}>
            <div onClick={() => searchBeats(0)} className={styles.genre}>
              <h3>All</h3>
              <img src={images.edm} alt="" />
            </div>
            {
              beatGenres && beatGenres.map(beatGenre => (
                <div onClick={() => searchBeats(beatGenre.id)} key={beatGenre.id} className={styles.genre}>
                  <h3>{beatGenre.name}</h3>
                  <img src={images[`genre${beatGenre.id}`]} alt="" />
                </div>
              ))
            }
          </div>
        </div>
      </div>
      <div className={styles['beat-cards']}>
        {postedBeats && postedBeats.map(beat => (
          <div key={beat.id} className={styles.card}>
            <div className={styles.left}>
              <div onClick={() => playOrPause(beat.id)} className={styles.button}>
                <img src={isPlaying === beat.id ? images.pause: images.play} alt="" />
              </div>
              <div className={styles.info}>
                <h4>{beat.beat.beatName} ({beat.price + '$'})</h4>
                <p>{beat.seller.artistName}</p>
              </div>
            </div>
            <div className={styles.right}>
                {isWaveSufferReady == beat.id && <div ref={waveformRef}></div>}
                {(sessionStorage.getItem('user') || localStorage.getItem('user')) && role == 'CUSTOMER' &&
                  <div  className={styles.heart}>
                    {beat.isFavorite? 
                      <img onClick={() => handleAddOrDeleteFavoriteArtist(beat.id, beat.isFavorite, beat.favoriteId)} 
                        src={images.heartLike}  />
                      :<img onClick={() => handleAddOrDeleteFavoriteArtist(beat.id, beat.isFavorite, beat.favoriteId)}  
                        src={images.heart}/> }
                  </div>
                }
                {(role == null || role == 'CUSTOMER') && 
                  <Link onClick={(e) => handleBuyBeat(beat.beat.id, beat.seller.id, e)} to='#'>
                    Buy Beat
                  </Link>
                }
            </div>
          </div>
        ))}
        {postedBeats && postedBeats.length < total &&
          <div onClick={handleLoadMore} className="load">
            <a>Load More</a>
          </div>
        }
      </div>
      <div className={styles.logos}>
          <div className={styles['logos-display']}>
            <div>
              <img src={images.logo1} alt="" />
            </div>
            <div>
              <img src={images.logo2} alt="" />
            </div>
            <div>
              <img src={images.logo3} alt="" />
            </div>
            <div>
              <img src={images.logo4} alt="" />
            </div>
            <div>
              <img src={images.logo5} alt="" />
            </div>
            <div>
              <img src={images.logo6} alt="" />
            </div>
          </div>
          <div className={styles['logos-display']}>
            <div>
              <img src={images.logo7} alt="" />
            </div>
            <div>
              <img src={images.logo8} alt="" />
            </div>
            <div>
              <img src={images.logo9} alt="" />
            </div>
            <div>
              <img src={images.logo10} alt="" />
            </div>
            <div>
              <img src={images.logo11} alt="" />
            </div>
            <div>
              <img src={images.logo12} alt="" />
            </div>
            <div>
              <img src={images.logo13} alt="" />
            </div>
            <div>
              <img src={images.logo14} alt="" />
            </div>
          </div>
        </div>
        {isForm && 
        <div id="post-beat" className={styles['post-beat']}>
          <form onSubmit={(e) =>handleSellBeat(e)}>
            <div onClick={turnOnOrOffForm} className={styles.out}>
              <span>X</span>
            </div>
            <div className={styles.row}>
              <label htmlFor='beatName'>Beat Name</label>
              <input type="text" name="beatName" id="beatName" placeholder="Enter Beat Name..." required />
            </div>
            <div className={styles.row}>
              <label htmlFor='beatFile' className={styles['label-file']}>{beatFileName}</label>
              <input type="file" name="beatFile" id="beatFile" accept=".mp3,audio/*" onChange={(e) =>changeFileName(e)}  />
            </div>
            <div className={styles.row}>
              <label htmlFor="jobType">Beat Genres</label>
              <select id="jobType" name="beatGenreId" required >
                {beatGenres && beatGenres.map(beatGenre => (
                  <option key={beatGenre.id} value={beatGenre.id}>{beatGenre.name}</option>
                ))}
              </select>
            </div>
            <div className={styles.row}>
              <label htmlFor="price">Price</label>
              <input type="text" name="price" id="price" placeholder="Enter price..." required />
            </div>
            <div className={styles.row}>
              <button type="submit">CONTINUTE</button>
            </div>
          </form>
        </div>
      }
      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </section>
  );
}
 
export default Beat;