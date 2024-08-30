import styles from "./ExploreContent.module.css";
import images from "../../../assets/images";
import { Link } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { Context } from "../../../common/ContextProvider";

const ExploreContent = () => {

  const [soundMakers, setSoundMakers] = useState([]);
  const [jobTypes, setJobTypes] = useState([]);
  const [isLoad, setIsLoad] = useState(true);
  const [isSearch, setIsSearch] = useState(false);
  const [total, setTotal] = useState(0);
  const {checkTokenAndRefreshToken, role, checkRole} = useContext(Context);
  const [favoriteArray, setFavoriteArray] = useState([]);

  useEffect(() => {
    setIsLoad(true);

    checkRole();

    let array = []

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
  
          await axios.get('http://localhost:8222/api/favorites/favorite-artist/find-by-account-owner-id/'+accountOwnerId, {
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

    const fetchSoundMaker = async() => {
      await wait(2000);
      await axios.get('http://localhost:8222/api/artists/sound-makers/find-all', {
        params:{
          page: Math.floor(soundMakers.length / 4)
        }
      })
      .then(async response => {
        setTotal(response.data.total);
        const soundMakerPromises = response.data.artists.map(async soundMaker => {
          try {
            const response = await axios.get('http://localhost:8222/api/reviews/find-by-expert-id/' + soundMaker.id);
            const data =  response.data.total;
            const isFavorite = array.length >0 ? array.some(row => row.artist.id == soundMaker.id): false;
            const favoriteId = array.filter(row => row.artist.id == soundMaker.id)[0];
            return {
                ...soundMaker,
                reviewsLength: data,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
        });

        const newSoundMakers = await Promise.all(soundMakerPromises);
        setSoundMakers(newSoundMakers);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      });
    }

    fetchSoundMaker();

    axios.get('http://localhost:8222/api/jobs/job-type')
    .then(async response => {
      setJobTypes(response.data);
    })
    .catch(error => {
      console.log(error);
    });

  },[]);

  const handleSearch = () => {
    setIsLoad(true);

    let artistName = document.getElementById('artist-name-search').value;
    let jobTypeId = document.getElementById('job-type-search').value;

    if(jobTypeId ==0 && artistName == ''){
      setIsSearch(false);
    }else {
      setIsSearch(true);
    }

    axios.get('http://localhost:8222/api/artists/sound-makers/find-by-skill-or-artist-name', {
      params: {
        'artist-name': artistName,
        'job-type-id': jobTypeId,
        page: 0
      }
    })
    .then(async response => {
      setTotal(response.data.total);
      const soundMakerPromises = response.data.artists.map(async soundMaker => {
        try {
          const response = await axios.get('http://localhost:8222/api/reviews/find-by-expert-id/' + soundMaker.id);
          const data =  response.data.total;
          const isFavorite = favoriteArray.length >0 ? favoriteArray.some(row => row.artist.id == soundMaker.id): false;
          const favoriteId = favoriteArray.filter(row => row.artist.id == soundMaker.id)[0];
          return {
              ...soundMaker,
              reviewsLength: data,
              isFavorite: isFavorite,
              favoriteId: favoriteId ? favoriteId.id: 0
          }
        } catch (error) {
            console.log(error);
            return null;
        }
    });

      const newSoundMakers = await Promise.all(soundMakerPromises);
      setSoundMakers(newSoundMakers);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  }

  const handleLoadMore= () => {
    setIsLoad(true);

    if(isSearch == false){
      axios.get('http://localhost:8222/api/artists/sound-makers/find-all', {
        params:{
          page: Math.floor(soundMakers.length / 4)
        }
      })
      .then(async response => {
        setTotal(response.data.total);
        const soundMakerPromises = response.data.artists.map(async soundMaker => {
          try {
            const response = await axios.get('http://localhost:8222/api/reviews/find-by-expert-id/' + soundMaker.id);
            const data =  response.data.total;
            const isFavorite = favoriteArray.length >0 ? favoriteArray.some(row => row.artist.id == soundMaker.id): false;
            const favoriteId = favoriteArray.filter(row => row.artist.id == soundMaker.id)[0];
            return {
                ...soundMaker,
                reviewsLength: data,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
      });

        const newSoundMakers = await Promise.all(soundMakerPromises);
        setSoundMakers(prevSoundMakers => [...prevSoundMakers, ...newSoundMakers]);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      });
    }else {
      let artistName = document.getElementById('artist-name-search').value;
      let jobTypeId = document.getElementById('job-type-search').value;

      axios.get('http://localhost:8222/api/artists/sound-makers/find-by-skill-or-artist-name', {
        params: {
          'artist-name': artistName,
          'job-type-id': jobTypeId,
          page: Math.floor(soundMakers.length / 4)
        }
      })
      .then(async response => {
        setTotal(response.data.total);
        const soundMakerPromises = response.data.artists.map(async soundMaker => {
          try {
            const response = await axios.get('http://localhost:8222/api/reviews/find-by-expert-id/' + soundMaker.id);
            const data =  response.data.total;
            const isFavorite = favoriteArray.length >0 ? favoriteArray.some(row => row.artist.id == soundMaker.id): false;
            const favoriteId = favoriteArray.filter(row => row.artist.id == soundMaker.id)[0];
            return {
                ...soundMaker,
                reviewsLength: data,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
      });

        const newSoundMakers = await Promise.all(soundMakerPromises);
        setSoundMakers(newSoundMakers);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      });
    }
  }

  const handleAddOrDeleteFavoriteArtist = async (artistId, isFavorite, favoriteId) => {
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
      await axios.delete('http://localhost:8222/api/favorites/favorite-artist/'+favoriteId, {
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('access_token')
        }
      })
      .then(() => {
        setSoundMakers(prev => 
          prev.map(soundMaker => 
            soundMaker.id === artistId 
              ? { ...soundMaker, isFavorite: !soundMaker.isFavorite, favoriteId: 0  }
              : soundMaker
          )
        );
        setFavoriteArray(prev => prev.filter(item => item.id !== favoriteId) );
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }else {
      await axios.post('http://localhost:8222/api/favorites/favorite-artist', {
        accountOwnerId: accountOwnerId,
        artistId: artistId
      } ,
      {
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setSoundMakers(prev => 
          prev.map(soundMaker => 
            soundMaker.id === artistId 
              ? { ...soundMaker, isFavorite: !soundMaker.isFavorite, favoriteId: response.data.id }
              : soundMaker
          )
        );
        setFavoriteArray(prev => [
          ...prev, 
          { id: response.data.id, accountOwner: { id: accountOwnerId }, artist: { id: response.data.artist.id } }
        ]);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }

  }

  return ( 
    <section className={styles.explore}>
      <div className={`${styles['container-input']}`}>
        <div>
          <div className={styles['input-search']}>
            <input id="artist-name-search" type="text" placeholder="Enter artist name..." />
            <img onClick={handleSearch} src={images.search} alt="" />
          </div>
          <div className={styles['select-search']}>
            <select id="job-type-search" >
              <option value="0">All</option>
              {jobTypes.map(jobType => (
                <option key={jobType.id} value={jobType.id}>{jobType.name}</option>
              ))}
            </select>
          </div>
        </div>
      </div>
      <div className={`${'container-common'} ${styles.cards}` }>
        {soundMakers && soundMakers.map(soundMaker => (
          <div key={soundMaker.id} className={styles.card}>
            <div className={styles.avatar}>
              <Link  to={`/artists/${soundMaker.id}`}>
                <img src={'http://localhost:8222/api/artists/images/'+soundMaker.image} alt="" />
              </Link>
            </div>
            <div className={styles['card-info']}>
              <Link  to={`/artists/${soundMaker.id}`}><h3>{soundMaker.artistName}</h3></Link>
              <div className={styles.country}>
                <span><Link  to={`/artists/${soundMaker.id}`}>{soundMaker.user.fullName}</Link>, {soundMaker.user.country}</span>
                {(sessionStorage.getItem('user') || localStorage.getItem('user')) && role == 'CUSTOMER' &&
                  <div onClick={() => handleAddOrDeleteFavoriteArtist(soundMaker.id, soundMaker.isFavorite,
                    soundMaker.favoriteId)} className={styles.favorite}>
                    {soundMaker.isFavorite? <img src={images.heartLike}  />: <img src={images.heart}/> }
                    <span>Add to favorites</span>
                  </div>
                }
              </div>
              <div className={styles.reviews}>
                <img src={images.star} alt="" />
                <img src={images.star} alt="" />
                <img src={images.star} alt="" />
                <img src={images.star} alt="" />
                <img src={images.star} alt="" />
                <span>({soundMaker.reviewsLength})</span>
              </div>
              <p className={styles.introduction}>
                {soundMaker.introduction} 
              </p>
            </div>
          </div>
        ))}
      </div>

      {soundMakers && soundMakers.length < total &&
        <div onClick={handleLoadMore} className="load">
          <a>Load More</a>
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
 
export default ExploreContent;