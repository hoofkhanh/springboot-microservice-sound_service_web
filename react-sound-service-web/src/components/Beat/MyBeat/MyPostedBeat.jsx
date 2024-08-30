import { useContext, useEffect, useState } from "react";
import styles from "./MyPostedBeat.module.css";
import { Context } from "../../../common/ContextProvider";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const MyPostedBeat = () => {
  const [myPostedBeat, setMyPostedBeat]= useState([]);

  const { checkTokenAndRefreshToken} = useContext(Context);
  const [isLoad, setIsLoad] = useState(true);


  const navigate = useNavigate();

  useEffect(() => {
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }
    
    let sellerId = null;
    if(localStorage.getItem('user')){
      sellerId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      sellerId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    if(!sellerId) {
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    const fetchMyBeats = async() => { 
      await axios.get('http://localhost:8222/api/beats/posted-beat/find-by-seller-id/'+sellerId)
      .then(response => {
        setMyPostedBeat(response.data);
      })
      .catch(error => {
        console.log(error);
      });
    }
  
    fetchMyBeats();
    setIsLoad(false);
  }, []);

  const deletePostedBeat = async (id) => {
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    await checkTokenAndRefreshToken();

    await axios.delete('http://localhost:8222/api/beats/posted-beat/'+id, {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('access_token')
      }
    })
    .then(() => {
      setMyPostedBeat(prev => prev.filter(postedBeat => postedBeat.id !== id));
    })  
    .catch(error => { 
      console.log(error);
    });

    setIsLoad(false);
  }


  return ( 
    <section className={styles.mybeat}>
      <div className="container-common">
        <div>
          <h1>My Posted Beats</h1>
          <div className={styles.table}>
            {myPostedBeat && myPostedBeat.map((postedBeat,index) => (
              <div key={postedBeat.id} className={styles.card}>
                <p className={styles.remove} onClick={() => deletePostedBeat(postedBeat.id)} >X</p>
                <p className={styles.index}>{index +1}</p>
                <p className={styles.topic}>{postedBeat.beat.beatName}: {postedBeat.price}$</p>
                <div className={styles.audio}>
                  <audio controls preload="auto" >
                    <source src={`http://localhost:8222/api/beats/beat/get-beat/${postedBeat.beat.beatFile}`} type="audio/ogg" />
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
 
export default MyPostedBeat;