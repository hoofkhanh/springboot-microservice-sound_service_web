import { Link } from "react-router-dom";
import styles from "./Explore.module.css";
import images from "../../../assets/images";
import { useContext } from "react";
import { Context } from "../../../common/ContextProvider";

const Explore = () => {
  const {displayVideo, handleClickWatchVideo} = useContext(Context);

  return ( 
    <div className={styles.container}>
      <div className={`${'container-common'} ${styles['big-info']}`}>
        <h1>Top Music Producers for hire</h1>
        <h2>Find the perfect music producer to arrange, record, hire live musicians, and mix your next hit song.
          (License Beats and <Link to="/beats">Beats Here</Link>)</h2>
        <div onClick={handleClickWatchVideo} className={styles['button-play-video']}>
          <button type="button">
            <img src={images.play} />
          </button>
          <span>Watch Video</span>
        </div>
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
 
export default Explore;