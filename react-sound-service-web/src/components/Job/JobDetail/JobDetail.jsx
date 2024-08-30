import styles from "./JobDetail.module.css";
import images from "../../../assets/images";
import audio from "../../../assets/Exist.mp3";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { Context } from "../../../common/ContextProvider";

const JobDetail = () => {
  const [jobDetail, setJobDetail] = useState(null);
  const [isLoad, setIsLoad] = useState(true);
  const {id} = useParams();

  const { checkTokenAndRefreshToken, role, checkRole, clientRef, isForm, turnOnOrOffForm} = useContext(Context);

  const [isHideContact, setIsHideContact] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    checkRole();
    setIsLoad(true);
    axios.get('http://localhost:8222/api/jobs/posted-job/'+id)
    .then(response => {
      setJobDetail(response.data);
      setIsLoad(false);
    }).catch(error => {
      console.log(error)
    });
  }, [])

  const handleContact = () => {
    const userInput = prompt("Nhập vào tin nhắn muốn gửi:");

    // Kiểm tra nếu người dùng nhập giá trị và không hủy bỏ
    if (userInput !== null) {
      if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
        navigate('/sign-in');
        return;
      }
  
      const checkTokenFunction = async () => {
        await checkTokenAndRefreshToken();
      }
  
      checkTokenFunction();
  
      let id = null;
      if(localStorage.getItem('user')){
        id = JSON.parse(localStorage.getItem('user')).id;
      }

      if(sessionStorage.getItem('user')){
        id = JSON.parse(sessionStorage.getItem('user')).id;
      }

      let idRole = id + ' ' + role    
  
      const request = {
        senderIdRole: idRole,
        receiverIdRole: jobDetail.poster.id + ' ' + jobDetail.poster.user.role,
        message: userInput
      }
  
      clientRef.current.send('/app/private', {}, JSON.stringify(request));
      window.location.reload();
    }
  }

  useEffect(() => {
    if(role) {
      if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
        navigate('/sign-in');
        return;
      }
      
      const checkTokenAndRefreshTokenFunction = async () => {
        await checkTokenAndRefreshToken();
      }

      checkTokenAndRefreshTokenFunction();

      let id = null;
      if(localStorage.getItem('user')){
        id = JSON.parse(localStorage.getItem('user')).id;
      }

      if(sessionStorage.getItem('user')){
        id = JSON.parse(sessionStorage.getItem('user')).id;
      }

      let idRole = id + ' ' + role 
      
      if(jobDetail && jobDetail.poster != null) {
        axios.get('http://localhost:8222/api/conversations/find-conversation-of-two-people/'+idRole+'/'+jobDetail.poster.id+' '+jobDetail.poster.user.role, {
          headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token')
          },
          params: {
            page: 0
          }
        })
        .then(response => {
          if(response.data.total ==0) {
            setIsHideContact(true);
          }else {
            setIsHideContact(false);
          }
        })
        .catch(error => {
          console.log(error);
        })
      }
    }
  }, [role, jobDetail]);

  return (
    <section className={styles.jobDetail}>
      <div className={`${"container-common"} ${styles['job-detail']}`}>
        <div className={styles.avatar}>
          <img src={jobDetail && jobDetail.poster.image? 
           'http://localhost:8222/api/customers/images/'+jobDetail.poster.image: images.defaultImage} alt="" />
          <p>by <Link to={jobDetail && `/customers/${jobDetail.poster.id}`}>{jobDetail && jobDetail.poster.user.fullName}</Link></p>
        </div>
        <div className={styles.content}>
          <h1>{jobDetail && jobDetail.topic}</h1>
          <p>
            {jobDetail && jobDetail.content}
          </p>
          {
            jobDetail && jobDetail.trackFile &&
            <div>
              <audio controls>
                {
                  jobDetail &&
                  <source src={`http://localhost:8222/api/jobs/posted-job/tracks/${jobDetail.trackFile}`} type="audio/mpeg" />
                }
              </audio>
            </div>
            }
        </div>
      </div>
      <div className={styles.contact}>
        {(role) && isHideContact == true && 
          <Link to='#' onClick={handleContact}>
            Contact <span>{jobDetail && jobDetail.poster.user.fullName}</span>
          </Link>
        }
      </div>
      
      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </section>
  );
};

export default JobDetail;
