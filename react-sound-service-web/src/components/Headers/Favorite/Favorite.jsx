import styles from "./Favorite.module.css";

const Favorite = () => {
  return ( 
    <div className={styles.container}>
      <div className={`${'container-common'} ${styles['big-info']}`}>
        <h1>My Favorites</h1>
      </div>
    </div>
   );
}
 
export default Favorite;