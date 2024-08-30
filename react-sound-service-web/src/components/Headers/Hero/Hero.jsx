import { Link } from "react-router-dom";
import images from "../../../assets/images";
import styles from "./Hero.module.css";
import { useContext, useEffect, useState } from "react";
import { Context } from "../../../common/ContextProvider";


const Hero = () => {

  const [imagesDisplay, setImagesDisplay] = useState([images.hero1, images.hero2, images.hero3, images.hero4]);
  const {displayVideo, handleClickWatchVideo} = useContext(Context);

  useEffect(() => {
    let index =0;
    const intervalId =  setInterval (function(){
      if(index == imagesDisplay.length ) {
        index =0;
      } 
      
      document.getElementById("hero-image").src = imagesDisplay[index];
      index++;
    } , 4000);

    return () => clearInterval(intervalId);
  }, [imagesDisplay]);


  return ( 
    <div className={styles.container}>
      <div className={styles['hero-info']}>
        <h1>Finish Your Song</h1>
        <h3>The world's best mixing & 
          mastering engineers, singers, songwriters, producers and 
          studio musicians for hire
        </h3>
        <div className={styles.service}>
          <Link to="#" className={styles['find-to-pro']}>
            Find A Pro
          </Link>
          <Link to="#" className={styles['post-job']}>
            Post Job
          </Link>
        </div>
        <div onClick={handleClickWatchVideo} className={styles['button-play-video']}>
            <button type="button">
              <img src={images.play} />
            </button>
            <span>Watch Video</span>
        </div>
      </div>
      <div className={styles['hero-image']}>
        <img id="hero-image" src={images.hero1}  />
      </div>

      {
        displayVideo == true && 
        <div className={styles['video-div']}>
          <div className={styles['screen-video']}>
            <video controls autoPlay> 
              <source src="https://dkxd2qj9i8fak.cloudfront.net/videos/SoundBetter.mp4"></source>
            </video>
            <button onClick={handleClickWatchVideo} type="button">X</button>
          </div>
        </div>
      }
    </div>
  );
}
 
export default Hero;