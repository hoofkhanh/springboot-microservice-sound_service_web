import { Link, useLocation } from "react-router-dom";
import styles from "./Footer.module.css";

const Footer = () => {
  const location = useLocation();
  const currentPath = location.pathname;

  return ( 
    <section className={styles.footer}>
      {
        currentPath == '/' && 
        <div className={styles['footer-1']}>
          <h1>Get started for free</h1>
          <button >GET QUOTES FROM PROS</button>
        </div>
      }
      <div className={styles['footer-2']}>
        <ul >
          <li>
            SoundBetter is a curated marketplace of the world's top music production talent
          </li>
          <li>
            info@soundbetter.com +(1) 888-734-4358
          </li>
        </ul>
        <ul className={styles.signup}>
          <li><Link to='#'>Sign up as a provider</Link></li>
          <li><Link to='#'>Sign in</Link></li>
          <li><Link to='#'>About Us</Link></li>
          <li><Link to='#'>User Reviews</Link></li>
          <li><Link to='#'>FAQ</Link></li>
        </ul>
        <ul className={styles.terms}>
          <li><Link to='#'>Term</Link></li>
          <li><Link to='#'>Tutorials</Link></li>
          <li><Link to='#'>Privacy</Link></li>
          <li><Link to='#'>Contact Us</Link></li> 
        </ul>
      </div>
    </section>
   );
}
 
export default Footer;