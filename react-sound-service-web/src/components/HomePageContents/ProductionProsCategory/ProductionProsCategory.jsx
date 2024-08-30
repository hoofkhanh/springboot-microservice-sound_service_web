import { Link } from "react-router-dom";
import images from "../../../assets/images";
import styles from "./ProductionProsCategory.module.css";

const ProductionProsCategory = () => {
  return ( 
    <section className={styles.container}>
      <h1>Discover Top Music Production Pros</h1>
      <div className={`${styles['container-grid']} ${'container-common'}`}>
        <div className={styles.category}>
          <Link to="#">
            <img src={images.Categories_Producers}  />
            <div className={styles['category-information']}>
              <h4>Producers</h4>
              <p>Hire and work with top producers ready to turn your song or idea into a hit</p>
            </div>
          </Link>
        </div>
        <div className={styles.category}>
          <Link to="#">
            <img src={images.Categories_Singers}  />
            <div className={styles['category-information']}>
              <h4>Singers</h4>
              <p>Discover hundreds of the industry's top singers and vocalists for hire in every genre, tone, and vibe</p>
            </div>
          </Link>
        </div>
        <div className={styles.category}>
          <Link to="/">
            <img src={images.Categories_Mixing_Engineers}  />
            <div className={styles['category-information']}>
              <h4>Mixing Engineers</h4>
              <p>Hire hit-making mixing engineers that will transform your recorded tracks into release-ready songs</p>
            </div>
          </Link>
        </div>
        <div className={styles.category}>
          <Link to="#">
            <img src={images.Categories_Songwriters}  />
            <div className={styles['category-information']}>
              <h4>Songwriters</h4>
              <p>Connect with hundreds of multi-platinum lyricists and songwriters for hire</p>
            </div>
          </Link>
        </div>
        <div className={styles.category}>
          <Link to="#">
            <img src={images.Categories_Mastering_Engineers}  />
            <div className={styles['category-information']}>
              <h4>Mastering Engineers</h4>
              <p>Award-winning mastering engineers in every price and genre for hire</p>
            </div>
          </Link>
        </div>
        <div className={styles.category}>
          <Link>
            <img src={images.Categories_Session_Musicians}  />
            <div className={styles['category-information']}>
              <h4>Session Musicians</h4>
              <p>Custom drum tracks, guitarists, bass players, string arrangers, and countless top instrumentalists to hire</p>
            </div>
          </Link>
        </div>
      </div>
      <div className={styles['more-categories']}>
        <Link to="#">More Categories</Link>
      </div>
    </section>
  );
}
 
export default ProductionProsCategory;