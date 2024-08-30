import styles from "./TermContent.module.css";
import images from "../../../assets/images";

const TermContent = () => {
  return (  
    <section className={styles.container}>
      <div className={styles.avartar}>
        <img src={images.avatar} />
      </div>
      <div className={styles['big-container-term']}>
        <h1>Over 400,000 musicians have used and trust SoundBetter</h1>

        <div className={styles.terms}>
          <ul>
            <li>
              <div className={styles['container-term']}>
                <div className={styles['term-image']}>
                  <img src={images.check} alt="" />
                </div>
                <div>
                  <h3>Safe and secure</h3>
                  <p>Fund a project to get started and release payment once it's completed</p>
                </div>
              </div>
            </li>
            <li>
              <div className={styles['container-term']}>
                <div className={styles['term-image']}>
                  <img src={images.check} alt="" />
                </div>
                <div>
                  <h3>Protect your copyright</h3>
                  <p>Our software provides a record of exchanges and files for future reference</p>
                </div>
              </div>
            </li>
            <li>
              <div className={styles['container-term']}>
                <div className={styles['term-image']}>
                  <img src={images.check} alt="" />
                </div>
                <div>
                  <h3>Protect your copyright</h3>
                  <p>Email, phone and chat to help you with your projects</p>
                </div>
              </div>
            </li>
            <li>
              <div className={styles['container-term']}>
                <div className={styles['term-image']}>
                  <img src={images.check} alt="" />
                </div>
                <div>
                  <h3>Simply the best talent</h3>
                  <p>SoundBetter pros work for their reviews and will give you their best</p>
                </div>
              </div>
            </li>
            <li>
              <div className={styles['container-term']}>
                <div className={styles['term-image']}>
                  <img src={images.check} alt="" />
                </div>
                <div>
                  <h3>Level up</h3>
                  <p>Better sounding content gets more plays, bookings and placement</p>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </section>
  );
}
 
export default TermContent;