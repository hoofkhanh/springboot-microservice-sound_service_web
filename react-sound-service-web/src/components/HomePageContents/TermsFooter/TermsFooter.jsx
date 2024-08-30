import { Link } from "react-router-dom";
import styles from "./TermsFooter.module.css";

const TermsFooter = () => {
  return ( 
    <section className={styles.container}>
      <div className={`${styles['terms']} ${'container-common'}`}>
        <ul>
          <li className={styles.title}>
            Popular Services
          </li>
          <li>
            <Link to='#'>
              Music Producers
            </Link>
          </li>
          <li>
            <Link to='#'>
              Female Singer
            </Link>
          </li>
          <li>
            <Link to='#'>
              Male Singer
            </Link>
          </li>
          <li>
            <Link to='#'>
              Mixing Engineers
            </Link>
          </li>
          <li>
            <Link to='#'>
              Music Producers
            </Link>
          </li>
          <li>
            <Link to='#'>
              Mastering Engineers
            </Link>
          </li>
          <li>
            <Link to='#'>
            Songwriters
            </Link>
          </li>
        </ul>
        <ul>
          <li className={styles.title}>
            Popular Locations
          </li>
          <li>
            <Link to='#'>
              New York
            </Link>
          </li>
          <li>
            <Link to='#'>
              Los Angeles
            </Link>
          </li>
          <li>
            <Link to='#'>
              Nashville
            </Link>
          </li>
          <li>
            <Link to='#'>
              London, UK
            </Link>
          </li>
          <li>
            <Link to='#'>
              Mixing Engineers in New York
            </Link>
          </li>
          <li>
            <Link to='#'>
              Mastering Studios in New York
            </Link>
          </li>
          <li>
            <Link to='#'>
              Session Singers in Nashville
            </Link>
          </li>
        </ul>
        <ul>
          <li className={styles.title}>
            Popular Genres
          </li>
          <li>
            <Link to='#'>
              Electronic
            </Link>
          </li>
          <li>
            <Link to='#'>
              Rock
            </Link>
          </li>
          <li>
            <Link to='#'>
              Pop
            </Link>
          </li>
          <li>
            <Link to='#'>
              EDM
            </Link>
          </li>
          <li>
            <Link to='#'>
              Heavy Metal
            </Link>
          </li>
          <li>
            <Link to='#'>
              Hip Hop
            </Link>
          </li>
          <li>
            <Link to='#'>
              House
            </Link>
          </li>
        </ul>
      </div>
    </section>
  );
}
 
export default TermsFooter;