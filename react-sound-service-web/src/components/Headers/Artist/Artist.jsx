import styles from "./Artist.module.css";
import images from "../../../assets/images";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";

const Artist = ({id}) => {
  const [artist, setArtist] = useState({});
  const [isLoad, setIsLoad] = useState(true);

  const [reviews, setReviews] = useState([]);

  useEffect( () => {
    setIsLoad(true);

     axios.get(`http://localhost:8222/api/artists/${id}`)
    .then(response => {
      setArtist(response.data);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  },[]);

  useEffect( () => {
    setIsLoad(true);

     axios.get(`http://localhost:8222/api/reviews/find-by-expert-id/${id}`)
    .then(response => {
      setReviews(response.data);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  },[]);

  return ( 
    <div className={styles.container}>
      <div className={styles['hero-info']}>
        <h1>{artist.artistName}</h1>
        <h3 >{artist.user && artist.user.fullName}</h3>
        <div className={styles.service}>
          <div className={styles.review}>
            <img src={images.star} alt="" />
            <img src={images.star} alt="" />
            <img src={images.star} alt="" />
            <img src={images.star} alt="" />
            <img src={images.star} alt="" />
          </div>
          <span>{reviews && reviews.length} Reviews</span>
        </div>
        <div className={styles.country}>
          <Link to='#'>{artist.user && artist.user.country}</Link>
        </div>
      </div>
      <div className={styles['hero-image']}>
        {artist && artist.image &&
          <img id="hero-image" src={`http://localhost:8222/api/artists/images/${artist.image}`}  />
        }
      </div>
      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </div>
  );
}
 
export default Artist;