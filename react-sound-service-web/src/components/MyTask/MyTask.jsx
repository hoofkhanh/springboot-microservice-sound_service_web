import { useContext, useEffect, useState } from "react";
import styles from "./MyTask.module.css";
import { Context } from "../../common/ContextProvider";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";

const MyTask = () => {
  const [myTasks, setMyTasks]= useState([]);
  const [myOldTasks, setMyOldTasks]= useState([]);

  const { checkTokenAndRefreshToken} = useContext(Context);
  const [isLoad, setIsLoad] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    setIsLoad(true);

    let artistType = null;
    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }else {
      
      if(localStorage.getItem('user')) {
        artistType =  JSON.parse(localStorage.getItem('user')).artistType;
      }

      if(sessionStorage.getItem('user')) {
        artistType =  JSON.parse(sessionStorage.getItem('user')).artistType;
      }
    }
    
    let expertId = null;
    if(localStorage.getItem('user')){
      expertId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      expertId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    if(!expertId) {
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    const fetchMyTask = async() => { 
      if(artistType == 'SOUND_MAKER') {
        await axios.get('http://localhost:8222/api/hires/task-of-valid-date-sound-maker/'+expertId, {
          headers: {
            Authorization: 'Bearer ' +localStorage.getItem('access_token')
          }
        })
        .then(response => {
          setMyTasks(response.data);
        })
        .catch(error => {
          console.log(error);
        });

        await axios.get('http://localhost:8222/api/hires/task-of-invalid-date-sound-maker/'+expertId, {
          headers: {
            Authorization: 'Bearer ' +localStorage.getItem('access_token')
          }
        })
        .then(response => {
          setMyOldTasks(response.data);
        })
        .catch(error => {
          console.log(error);
        });
      }else if(artistType == 'SOUND_CHECKER'){
        await axios.get('http://localhost:8222/api/hires/task-of-valid-date-sound-checker/'+expertId, {
          headers: {
            Authorization: 'Bearer ' +localStorage.getItem('access_token')
          }
        })
        .then(response => {
          setMyTasks(response.data);
        })
        .catch(error => {
          console.log(error);
        });

        await axios.get('http://localhost:8222/api/hires/task-of-invalid-date-sound-checker/'+expertId, {
          headers: {
            Authorization: 'Bearer ' +localStorage.getItem('access_token')
          }
        })
        .then(response => {
          setMyOldTasks(response.data);
        })
        .catch(error => {
          console.log(error);
        });
      }
     
    }
  
    fetchMyTask();
    setIsLoad(false);
  }, []);

  return ( 
    <section className={styles.myTask}>
      <div className="container-common">
        <div>
          <h1>My Task</h1>
          <div className={styles.table}>
            {myTasks && myTasks.map((task,index) => (
              <div key={task.id} className={styles.card}>
                <p className={styles.index}>{index +1}</p>
                <Link to={`/customers/${task.hirer.id}`}>
                  <p className={styles.topic}>{task.hirer.user.fullName}</p> 
                </Link>
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
          <h1>My Old Task</h1>
          <div className={styles.table}>
            {myOldTasks && myOldTasks.map((oldTask,index) => (
              <div key={oldTask.id} className={styles.card}>
                <p className={styles.index}>{index +1}</p>
                <Link to={`/customers/${oldTask.hirer.id}`}>
                  <p className={styles.topic}>{oldTask.hirer.user.fullName}</p> 
                </Link>
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
 
export default MyTask;