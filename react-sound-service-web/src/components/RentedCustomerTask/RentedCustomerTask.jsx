import { useNavigate } from "react-router-dom";
import styles from "./RentedCustomerTask.module.css";
import { useContext, useEffect, useState } from "react";
import { Context } from "../../common/ContextProvider";
import axios from "axios";

const RentedCustomerTask = () => {
  const [soundMakerTasks, setSoundMakerTasks]= useState([]);
  const [soundCheckerTasks, setSoundCheckerTasks]= useState([]);
  
  const [oldSoundMakerTasks, setOldSoundMakerTasks]= useState([]);
  const [oldSoundCheckerTasks, setOldSoundCheckerTasks]= useState([]);

  const { checkTokenAndRefreshToken} = useContext(Context);
  const [isLoad, setIsLoad] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }
    
    let hirerId = null;
    if(localStorage.getItem('user')){
      hirerId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      hirerId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    const fetchMyTask = async() => { 
      await axios.get('http://localhost:8222/api/hires/task-of-valid-date-sound-maker-of-hirer/'+hirerId, {
        headers: {
          Authorization: 'Bearer ' +localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setSoundMakerTasks(response.data);
      })
      .catch(error => {
        console.log(error);
      });

      await axios.get('http://localhost:8222/api/hires/task-of-invalid-date-sound-maker-of-hirer/'+hirerId, {
        headers: {
          Authorization: 'Bearer ' +localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setOldSoundMakerTasks(response.data);
      })
      .catch(error => {
        console.log(error);
      });
      
      await axios.get('http://localhost:8222/api/hires/task-of-valid-date-sound-checker-of-hirer/'+hirerId, {
        headers: {
          Authorization: 'Bearer ' +localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setSoundCheckerTasks(response.data);
      })
      .catch(error => {
        console.log(error);
      });

      await axios.get('http://localhost:8222/api/hires/task-of-invalid-date-sound-checker-of-hirer/'+hirerId, {
        headers: {
          Authorization: 'Bearer ' +localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setOldSoundCheckerTasks(response.data);
      })
      .catch(error => {
        console.log(error);
      });
     
    }
  
    fetchMyTask();
    setIsLoad(false);
  }, []);

  

  return ( 
    <section className={styles.myTask}>
      <div className="container-common">
        <div className={styles.task}>
          <h1>Hired Task (Sound Maker)</h1>
          <div className={styles.table}>
            {soundMakerTasks && soundMakerTasks.map((task,index) => (
              <div key={task.id} className={styles.card}>
                <p className={styles.index}>{index +1}</p>
                <p className={styles.topic}>{task.price}$</p> 
                <p className={styles.topic}>{task.startDate}</p> 
                <p className={styles.topic}>{task.endDate}</p> 
                {task.jobType &&
                  <p className={styles.topic}>{task.jobType.name}</p> 
                }
              </div>
            ))}
          </div>
        </div>
        <div className={styles.task}> 
          <h1>Hired Task (Sound Checker)</h1>
          <div className={styles.table}>
            {soundCheckerTasks && soundCheckerTasks.map((task,index) => (
              <div key={task.id} className={styles.card}>
                <p className={styles.index}>{index +1}</p>
                <p className={styles.topic}>{task.price}$</p> 
                <p className={styles.topic}>{task.startDate}</p> 
                <p className={styles.topic}>{task.endDate}</p> 
                {task.jobType &&
                  <p className={styles.topic}>{task.jobType.name}</p> 
                }
              </div>
            ))}
          </div>
        </div>
        <div className={styles.oldTask}>
          <h1>Old Task (Sound Maker)</h1>
          <div className={styles.table}>
            {oldSoundMakerTasks && oldSoundMakerTasks.map((oldTask,index) => (
              <div key={oldTask.id} className={styles.card}>
                <p className={styles.index}>{index +1}</p>
                <p className={styles.topic}>{oldTask.price}$</p> 
                <p className={styles.topic}>{oldTask.startDate}</p> 
                <p className={styles.topic}>{oldTask.endDate}</p> 
                {oldTask.jobType &&
                  <p className={styles.topic}>{oldTask.jobType.name}</p> 
                }
              </div>
            ))}
          </div>
        </div>
        <div className={styles.oldTask}>
          <h1>Old Task (Sound Checker)</h1>
          <div className={styles.table}>
            {oldSoundCheckerTasks && oldSoundCheckerTasks.map((oldTask,index) => (
              <div key={oldTask.id} className={styles.card}>
                <p className={styles.index}>{index +1}</p>
                <p className={styles.topic}>{oldTask.price}$</p> 
                <p className={styles.topic}>{oldTask.startDate}</p> 
                <p className={styles.topic}>{oldTask.endDate}</p> 
                {oldTask.jobType &&
                  <p className={styles.topic}>{oldTask.jobType.name}</p> 
                }
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
 
export default RentedCustomerTask;