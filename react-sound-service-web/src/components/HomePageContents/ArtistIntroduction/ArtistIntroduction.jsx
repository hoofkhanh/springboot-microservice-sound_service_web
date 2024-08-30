import { useEffect, useState } from "react";
import styles from "./ArtistIntroduction.module.css";

const ArtistIntroduction = () => {
  const [artistIntroduction, setArtistIntroduction] = useState(
    ["Zendaya's Engineer?", "Taylor Swift's Drummer?", "BTS' Mixing Engineer", 
      "Beyonce's Songwriter?", "John Mayer's Bass Player?",
      "Morrissey's Guitarist?", "The Killers' Mixing Engineer"
    ]
  );
  const [displayText, setDisplayText] = useState("");


  useEffect(() => {
    let index=0;
    
    setInterval (function(){
      if(index == artistIntroduction.length ) {
        index =0;
      } 

      setDisplayText(artistIntroduction[index]);
      index++;
    } , 4000);
  }, [artistIntroduction]);

  return ( 
    <section className={styles.container}>
      <div className={`${styles['container-introduction']} $'container-common'`}>
        <h1>Want to work with</h1>
        <h1 key={displayText} className={styles['change-text']} >{displayText}</h1>
        <p>Now you can, through SoundBetter</p>
      </div>
    </section>
  );
}
 
export default ArtistIntroduction;