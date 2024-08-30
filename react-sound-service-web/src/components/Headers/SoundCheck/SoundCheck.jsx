import styles from "./SoundCheck.module.css";

const SoundCheck = () => {
  return ( 
    <div className={styles.container}>
      <div className={`${'container-common'} ${styles['big-info']}`}>
        <h1>SoundCheck</h1>
        <h2>Get in-depth feedback and advice on your song, production or mix from top industry pros</h2>
      </div>
    </div>
  );
}
 
export default SoundCheck;