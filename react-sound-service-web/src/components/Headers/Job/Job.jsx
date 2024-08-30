import { useContext, useEffect, useState } from "react";
import { Context } from "../../../common/ContextProvider";
import styles from "./Job.module.css";

const Job = () => {
  const { turnOnOrOffForm, role, checkRole} = useContext(Context);

  useEffect(() => {
    checkRole();
  }, []);

  return ( 
    <div className={styles.container}>
      <div className={`${'container-common'} ${styles['big-info']}`}>
        <h1>Music Production & Engineering Job Board</h1>
        <h2>Sample of Recent Jobs</h2>
        <div  className={styles['button-play-video']}>
          {(role == null || role == 'CUSTOMER') && 
            <button onClick={turnOnOrOffForm} type="button">
              Post Your Job Now
            </button>
          }
        </div>
      </div>
    </div>
  );
}
  
export default Job;