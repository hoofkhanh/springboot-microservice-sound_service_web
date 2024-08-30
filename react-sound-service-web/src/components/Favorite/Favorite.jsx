import styles from "./Favorite.module.css";
import images from "../../assets/images";
import { useEffect, useState } from "react";
import { useContext } from "react";
import { Context } from "../../common/ContextProvider";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";

const Favorite = () => {
  const [favorite, setFavorite] = useState([]);
  const [favoriteType, setFavoriteType] = useState('artist');
  const {checkTokenAndRefreshToken} = useContext(Context);
  const [isLoad, setIsLoad] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    const checkTokenAndRefreshTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenAndRefreshTokenFunction();

    let accountOwnerId = null;
    if(localStorage.getItem('user')){
      accountOwnerId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      accountOwnerId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    const fetchData = async () => {
      if(favoriteType == 'artist') {
        await axios.get('http://localhost:8222/api/favorites/favorite-artist/find-by-account-owner-id/'+accountOwnerId, {
          headers: {
            'Authorization': 'Bearer '+ localStorage.getItem('access_token')
          }
        })
        .then(response => {
          setFavorite(response.data.map(item => ({
            ...item,
            isFavorite: true
          })));
          setIsLoad(false);
        })
        .catch(error => {
          console.log(error);
        });
      }else {
        await axios.get('http://localhost:8222/api/favorites/favorite-beat/find-by-account-owner-id/'+accountOwnerId, {
          headers: {
            'Authorization': 'Bearer '+ localStorage.getItem('access_token')
          }
        })
        .then(response => {
          setFavorite(response.data.map(item => ({
            ...item,
            isFavorite: true
          })));
          setIsLoad(false);
        })
        .catch(error => {
          console.log(error);
        });
      }
    } 

    fetchData();
  }, [favoriteType]);

  const handleAddOrDeleteFavoriteArtist = async (artistOrPostedBeatId, isFavorite, favoriteId) => {
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
      await axios.delete(`http://localhost:8222/api/favorites/favorite-${favoriteType}/${favoriteId}`, {
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('access_token')
        }
      })
      .then(() => {
        setFavorite(prev => 
          prev.map(favorite => 
            favorite.id === favoriteId 
              ? { ...favorite, isFavorite: !favorite.isFavorite }
              : favorite
          )
        );
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }else {
      let request = null;
      if(favoriteType == 'artist'){
        request = {
          accountOwnerId: accountOwnerId,
          artistId: artistOrPostedBeatId
        }
      }else {
        request = {
          accountOwnerId: accountOwnerId,
          postedBeatId: artistOrPostedBeatId
        }
      }
      await axios.post(`http://localhost:8222/api/favorites/favorite-${favoriteType}`, request ,
      {
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setFavorite(prev => 
          prev.map(favorite => 
            favorite.id === favoriteId 
              ? { ...favorite, isFavorite: !favorite.isFavorite, id: response.data.id }
              : favorite
          )
        );

        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }
  }

  return (  
    <section >
      <div className={`${'container-common'} ${styles.favorite}`}>
        <div className={styles.filter}>
          <p onClick={() => setFavoriteType('artist')}>Artist</p>
          <p onClick={() => setFavoriteType('beat')}>Beat</p>
        </div>
        <div className={styles['big-favorite']}>
          {favorite && favorite.map(favorite => (
            <div key={favorite.id} className={styles.card}>
              <div className={styles.avatar}>
                {favorite.artist && favorite.artist.artistType == 'SOUND_MAKER' &&
                  <Link  to={`/artists/${favorite.artist.id}`}>
                    <img src={favorite.artist ? `http://localhost:8222/api/artists/images/${favorite.artist.image}`: null} alt="" />
                  </Link>
                }
                {favorite.artist && favorite.artist.artistType == 'SOUND_CHECKER' &&
                  <Link  to={`/sound-checkers/${favorite.artist.id}`}>
                    <img src={favorite.artist ? `http://localhost:8222/api/artists/images/${favorite.artist.image}`: null} alt="" />
                  </Link>
                }
                {favorite.postedBeat && 
                  <Link  to='/beats'>
                    <img src={favorite.postedBeat ? images.dvd: null} alt="" />
                  </Link>
                }
                
              </div>
              <div className={styles.name}>
                <div className={styles.info}>
                  {favorite.artist && 
                    <div onClick={() => handleAddOrDeleteFavoriteArtist(favorite.artist.id, favorite.isFavorite, favorite.id)} className={styles.heart}>
                      {favorite.isFavorite ? <img src={images.heartLike} alt="" />: <img src={images.heart} alt="" />}
                      
                      <span>Remove favorite</span>
                    </div>
                  }
                  {favorite.postedBeat && 
                    <div onClick={() => handleAddOrDeleteFavoriteArtist(favorite.postedBeat.id, favorite.isFavorite, favorite.id)} className={styles.heart}>
                      {favorite.isFavorite ? <img src={images.heartLike} alt="" />: <img src={images.heart} alt="" />}
                    
                    <span>Remove favorite</span>
                  </div>
                  }
                  {favorite.artist && favorite.artist.artistType == 'SOUND_MAKER' &&
                    <Link  to={`/artists/${favorite.artist.id}`}>
                      <h4>{favorite.artist.artistName}</h4>
                    </Link>
                  }
                  {favorite.artist && favorite.artist.artistType == 'SOUND_CHECKER' &&
                    <Link  to={`/sound-checkers/${favorite.artist.id}`}>
                      <h4>{favorite.artist.artistName}</h4>
                    </Link>
                  }
                  {favorite.postedBeat &&
                    <Link to='/beats'>
                      <h4>{favorite.postedBeat.beat.beatName}: {favorite.postedBeat.price}$</h4>
                    </Link>
                  }
                </div>
              </div>
            </div>
          ))}
          
        </div>
      </div>
      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </section>
  );
}
 
export default Favorite;