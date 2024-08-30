import styles from "./SoundCheck.module.css";
import images from "../../assets/images";
import { Link } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { Context } from "../../common/ContextProvider";

const SoundCheck = () => {
  const [soundCheckers, setSoundCheckers] = useState([]);
  const [isLoad, setIsLoad] = useState(true);
  const [isSearch, setIsSearch] = useState(false);
  const [total, setTotal] = useState(0);
  const {checkTokenAndRefreshToken, role, checkRole} = useContext(Context);
  const [favoriteArray, setFavoriteArray] = useState([]);

  useEffect(() => {
    let array = []
    setIsLoad(true);

    checkRole();

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

    const fetchSoundChecker =  async() => {
      await wait(2000);
      await axios.get('http://localhost:8222/api/artists/sound-checkers/find-all', {
        params:{
          page: Math.floor(soundCheckers.length / 3)
        }
      })
      .then(async response => {
        setTotal(response.data.total);
        const soundCheckerPromises = response.data.artists.map(async soundCheck => {
          try {
            const response = await axios.get('http://localhost:8222/api/reviews/find-by-expert-id/' + soundCheck.id);
            const data =  response.data.total;
            const isFavorite = array.length >0 ? array.some(row => row.artist.id == soundCheck.id): false;
            const favoriteId = array.filter(row => row.artist.id == soundCheck.id)[0];
            return {
                ...soundCheck,
                reviewsLength: data,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
      });
      
        const newSoundCheckers = await Promise.all(soundCheckerPromises);
        setSoundCheckers(newSoundCheckers);
        setIsLoad(false);
      })
    }

    fetchSoundChecker();
  }, []);

  const handleSearch = () => {
    setIsLoad(true);

    let artistName = document.getElementById('artist-name-search').value;

    if(artistName == ''){
      setIsSearch(false);
    }else {
      setIsSearch(true);
    }

    axios.get('http://localhost:8222/api/artists/sound-checkers/find-by-artist-name', {
      params: {
        'artist-name': artistName,
        page: 0
      }
    })
    .then(async response => {
      setTotal(response.data.total);
      const soundCheckerPromises = response.data.artists.map(async soundCheck => {
        try {
          const response = await axios.get('http://localhost:8222/api/reviews/find-by-expert-id/' + soundCheck.id);
          const data =  response.data.total;
          const isFavorite = favoriteArray.length >0 ? favoriteArray.some(row => row.artist.id == soundCheck.id): false;
          const favoriteId = favoriteArray.filter(row => row.artist.id == soundCheck.id)[0];
          return {
              ...soundCheck,
              reviewsLength: data,
              isFavorite: isFavorite,
              favoriteId: favoriteId ? favoriteId.id: 0
          }
        } catch (error) {
            console.log(error);
            return null;
        }
     });
      const newSoundCheckers = await Promise.all(soundCheckerPromises);
      setSoundCheckers(newSoundCheckers);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  }

  const handleLoadMore = () => {
    setIsLoad(true);

    if(isSearch == false){
      axios.get('http://localhost:8222/api/artists/sound-checkers/find-all', {
        params:{
          page: Math.floor(soundCheckers.length / 3)
        }
      })
      .then(async response => {
        setTotal(response.data.total);
        const soundCheckerPromises = response.data.artists.map(async soundCheck => {
          try {
            const response = await axios.get('http://localhost:8222/api/reviews/find-by-expert-id/' + soundCheck.id);
            const data =  response.data.total;
            const isFavorite = favoriteArray.length >0 ? favoriteArray.some(row => row.artist.id == soundCheck.id): false;
            const favoriteId = favoriteArray.filter(row => row.artist.id == soundCheck.id)[0];
            return {
                ...soundCheck,
                reviewsLength: data,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
      });
        const newSoundCheckers = await Promise.all(soundCheckerPromises);
        setSoundCheckers(prev => [...prev, ...newSoundCheckers]);
        setIsLoad(false);
      })
    }else {
      let artistName = document.getElementById('artist-name-search').value;

      axios.get('http://localhost:8222/api/artists/sound-checkers/find-by-artist-name', {
        params: {
          'artist-name': artistName,
          page: Math.floor(soundCheckers.length / 3)
        }
      })
      .then(async response => {
        setTotal(response.data.total);
        const soundCheckerPromises = response.data.artists.map(async soundCheck => {
          try {
            const response = await axios.get('http://localhost:8222/api/reviews/find-by-expert-id/' + soundCheck.id);
            const data =  response.data.total;
            const isFavorite = favoriteArray.length >0 ? favoriteArray.some(row => row.artist.id == soundCheck.id): false;
            const favoriteId = favoriteArray.filter(row => row.artist.id == soundCheck.id)[0];
            return {
                ...soundCheck,
                reviewsLength: data,
                isFavorite: isFavorite,
                favoriteId: favoriteId ? favoriteId.id: 0
            }
          } catch (error) {
              console.log(error);
              return null;
          }
      });
        const newSoundCheckers = await Promise.all(soundCheckerPromises);
        setSoundCheckers(prev => [...prev, ...newSoundCheckers]);
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
        setSoundCheckers(prev => 
          prev.map(soundChecker => 
            soundChecker.id === artistId 
              ? { ...soundChecker, isFavorite: !soundChecker.isFavorite, favoriteId: 0  }
              : soundChecker
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
        setSoundCheckers(prev => 
          prev.map(soundChecker => 
            soundChecker.id === artistId 
              ? { ...soundChecker, isFavorite: !soundChecker.isFavorite, favoriteId: response.data.id }
              : soundChecker
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
    <section className={styles.soundCheck}>
      <div className={`${styles['container-input']}`}>
        <div>
          <div className={styles['input-search']}>
            <input id="artist-name-search" type="text" placeholder="Enter artist name..." />
            <img onClick={handleSearch} src={images.search} alt="" />
          </div>
        </div>
      </div>
      <div className={`${'container-common'} ${styles.cards}` }>
        {soundCheckers && soundCheckers.map(soundChecker => (
          <div key={soundChecker.id} className={styles.card}>
            <div className={styles.avatar}>
              <Link  to={`/sound-checkers/${soundChecker.id}`}>
                <img 
                  src={soundChecker ? 'http://localhost:8222/api/artists/images/'+soundChecker.image: 
                  images.defaultImage} alt="" />
              </Link>
            </div>
            <div className={styles['card-info']}>
              <Link  to={`/sound-checkers/${soundChecker.id}`}><h3>{soundChecker.artistName}</h3></Link>
              <div className={styles.country}>
                <span>
                  <Link  to={`/sound-checkers/${soundChecker.id}`}>{soundChecker.user.fullName}</Link>, {soundChecker.user.country}
                </span>
                {(sessionStorage.getItem('user') || localStorage.getItem('user')) && role == 'CUSTOMER' && 
                  <div onClick={() => handleAddOrDeleteFavoriteArtist(soundChecker.id, soundChecker.isFavorite,
                    soundChecker.favoriteId)} className={styles.favorite}>
                    {soundChecker.isFavorite? <img src={images.heartLike}  />: <img src={images.heart}/> }
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
                <span>({soundChecker.reviewsLength})</span>
              </div>
              <p className={styles.introduction}>
                {soundChecker.introduction}
              </p>
            </div>
          </div>
        ))}
      </div>
      
      {soundCheckers && soundCheckers.length < total && 
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
 
export default SoundCheck;