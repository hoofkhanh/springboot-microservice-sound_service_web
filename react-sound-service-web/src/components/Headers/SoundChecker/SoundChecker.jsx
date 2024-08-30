import styles from "./SoundChecker.module.css";
import images from "../../../assets/images";
import { Link } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { Context } from "../../../common/ContextProvider";

const SoundChecker = ({id}) => {

  const [soundChecker, setSoundChecker] = useState(null);
  const [isLoad, setIsLoad] = useState(true);

  const { checkTokenAndRefreshToken, role, checkRole} = useContext(Context);
  const [isFavorite, setIsFavorite] = useState(false);
  const [favoriteId, setFavoriteId] = useState(false);

  useEffect(() => {
    setIsLoad(true);

    checkRole();

    const fetchFavorite = async () => {
      if((localStorage.getItem('user') || sessionStorage.getItem('user')) && role == 'CUSTOMER' ) {
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
            const isFavorite = response.data.some(item => item.artist.id === id);
            setIsFavorite(isFavorite);

            const favoriteId = response.data.filter(row => row.artist.id == id)[0];
            setFavoriteId(favoriteId ? favoriteId.id: 0);

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

    axios.get('http://localhost:8222/api/artists/'+id)
    .then(response => {
      setSoundChecker(response.data);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  }, [role]);

  const handleAddOrDeleteFavoriteArtist = async (artistId, favoriteId, e) => {
    e.preventDefault();

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
        setIsFavorite(prev => !prev);
        setFavoriteId(0);
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
        setIsFavorite(prev => !prev);
        setFavoriteId(response.data.id);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }

  }

  return ( 
    <div className={styles.container}>
      <div className={styles['hero-info']}>
        <h1>SoundCheck with {soundChecker && soundChecker.artistName}</h1>
        <p>Genres</p>
        <h3>
          {soundChecker && soundChecker.skills.map((skill, index) => (
            <span key={skill.jobTypeId}>
              {skill.name}{index != soundChecker.skills.length -1 && ', '}
            </span>
          ))}
        </h3>
        <div className={styles.service}>
          <p>Offers feedback on</p>
          <h3>
            Mixing, Arrangement, Lyrics, Mastering, Instrumentation, Melody, Production, Vocals, Songwriting, Recording, Performance
          </h3>
        </div>
        {
          role && role == 'CUSTOMER' &&
          <Link to='#' onClick={(e) => handleAddOrDeleteFavoriteArtist(soundChecker.id, favoriteId, e)} className={styles.favorite}>
            {isFavorite ? <img src={images.heartLike} alt="" /> : <img src={images.heart} alt="" />}
          </Link>
        }
      </div>
      <div className={styles['hero-image']}>
        <img id="hero-image" src={soundChecker && `http://localhost:8222/api/artists/images/${soundChecker.image}`}  />
      </div>
      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </div>
  );
}
 
export default SoundChecker;