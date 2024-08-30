import { Link } from "react-router-dom";
import styles from "./Beat.module.css";
import { useContext, useEffect } from "react";
import { Context } from "../../../common/ContextProvider";

const Beat = () => {
  const {turnOnOrOffForm, role, checkRole} = useContext(Context);

  useEffect(() => {
    checkRole();
  }, [])

  return (
    <div className={styles.container}>
      <div className={`${"container-common"} ${styles["big-info"]}`}>
        <h1>SoundBetter Tracks</h1>
        <h2>
          License hand-picked tracks & beats from top industry producers Find a
          track you love, write a topline and release your song Exclusive
          licenses for commercial release
        </h2>
        {(role == null || role == 'ARTIST') &&
          <Link to='#' onClick={turnOnOrOffForm}>Sell Beat</Link>
        }
      </div>
    </div>
  );
};

export default Beat;
