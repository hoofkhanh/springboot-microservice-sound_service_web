import { Link, useNavigate } from "react-router-dom";
import styles from "./MyPurchasedBeat.module.css";
import { useContext, useEffect, useState } from "react";
import { Context } from "../../../common/ContextProvider";
import axios from "axios";

const MyPurchasedBeat = () => {
  const [myPurchasedBeats, setMyPurchasedBeats]= useState([]);

  const { checkTokenAndRefreshToken} = useContext(Context);
  const [isLoad, setIsLoad] = useState(true);


  const navigate = useNavigate();

  useEffect(() => {
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }
    
    let purchaserId = null;
    if(localStorage.getItem('user')){
      purchaserId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      purchaserId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    if(!purchaserId) {
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    const fetchMyPurchasedBeats = async() => { 
      await axios.get('http://localhost:8222/api/purchased-beats/purchased-beat-of-purchaser/'+purchaserId, {
        headers: {
          Authorization: 'Bearer ' +localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setMyPurchasedBeats(response.data);
      })
      .catch(error => {
        console.log(error);
      });
    }
  
    fetchMyPurchasedBeats();
    setIsLoad(false);
  }, []);

  return ( 
    <section className={styles.myPurchasedBeat}>
      <div className="container-common">
        <div>
          <h1>My Purchased Beats</h1>
          <div className={styles.table}>
            {myPurchasedBeats && myPurchasedBeats.map((purchasedBeat,index) => (
              <div key={purchasedBeat.id} className={styles.card}>
                <p className={styles.index}>{index +1}</p>
                {purchasedBeat.seller.artistType == 'SOUND_MAKER' &&
                  <Link to={`/artists/${purchasedBeat.seller.id}`}>
                    <p className={styles.topic}>{purchasedBeat.seller.artistName}</p>
                  </Link>
                }
                {purchasedBeat.seller.artistType == 'SOUND_CHECKER' &&
                  <Link to={`/sound-checkers/${purchasedBeat.seller.id}`}>
                    <p className={styles.topic}>{purchasedBeat.seller.artistName}</p>
                  </Link>
                }
                <p className={styles.topic}>{purchasedBeat.beat.beatName}</p>
                <div className={styles.audio}>
                  <audio controls preload="auto" >
                    <source src={`http://localhost:8222/api/beats/beat/get-beat/${purchasedBeat.beat.beatFile}`} type="audio/ogg" />
                  </audio>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </section>
   );
}
 
export default MyPurchasedBeat;