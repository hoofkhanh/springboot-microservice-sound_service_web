import styles from "./SeeWhyContent.module.css";
import images from "../../../assets/images";
import { useContext } from "react";
import { Context } from "../../../common/ContextProvider";

const SeeWhyContent = () => {
  const { handleClickWatchVideo} = useContext(Context);
  
  return ( 
    <section className={styles.container}>
      <div className={styles['big-image']}>
        <img src={images.soundBetterImage} alt="" />
        <div className={styles['image-info']}>
          <h1>See why the music industry’s best choose SoundBetter</h1>
          <div onClick={handleClickWatchVideo}>
            <button type="button" className={styles['button-play-video']}>
              <img src={images.play} />
            </button>
            <span>Hear from the pros</span>
          </div>
        </div>
      </div>
    </section>
  );
}
 
export default SeeWhyContent;