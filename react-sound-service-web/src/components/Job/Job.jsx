import styles from "./Job.module.css";
import images from "../../assets/images";
import { Link, useNavigate } from "react-router-dom";
import Pagination from "./Pagination";
import { useContext, useEffect, useState } from "react";
import { Context } from "../../common/ContextProvider";
import axios from "axios";

const Job = () => {

  const { isForm , turnOnOrOffForm, checkTokenAndRefreshToken, role, checkRole} = useContext(Context);
  const [isLoad, setIsLoad] = useState(true);
  const [jobTypes, setJobTypes] = useState([]);
  const [postedJobs, setPostedJobs] = useState([]);
  const [total, setTotal] = useState(0);
  const [isFilter, setIsFilter] = useState(false);
  const [filteredJobType, setFilteredJobType] = useState(0);
  const [trackFileName, setTrackFileName] = useState('Track File (Optional)');

  const [indexPass, setIndexPass] = useState(0);

  const navigate = useNavigate();

  

  useEffect(() => {
    setIsLoad(true);
    
    const fetchAllGenreJob = async () => {
      await axios.get('http://localhost:8222/api/jobs/job-type')
      .then(async response => {
        setJobTypes(response.data);
      })
      .catch(error => {
        console.log(error);
      });
    }

    const fetchAllPostedJob = async () => {
      await axios.get('http://localhost:8222/api/jobs/posted-job', {
        params: {
          page: 0
        }
      })
      .then(async response => {
        setTotal(response.data.total);
        setIndexPass(prev => prev +1);
        setPostedJobs(response.data.postedJobs);
      })
      .catch(error => {
        console.log(error);
      });
    }

    fetchAllGenreJob();

    fetchAllPostedJob();

    checkRole();

    setIsLoad(false);
  },[]);

  useEffect(() => {
    setTrackFileName('Track File (Optional)');
  }, [isForm]);

  const filterGenreJob = async (e) => {
    setIsLoad(true);
    const genreJob = e.target.getAttribute('value');

    if(genreJob ==0) {
      setIsFilter(false);
    }else {
      setFilteredJobType(genreJob);
      setIsFilter(true);
    }
    
    await axios.get('http://localhost:8222/api/jobs/posted-job/find-by-job-type-id/'+genreJob, {
      params: {
        page: 0
      }
    })
    .then(async response => {
      setTotal(response.data.total);
      setPostedJobs(response.data.postedJobs);
      setIndexPass(prev => prev +1);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  }

  window.onscroll = function () {
    scrollFunction();
  };

  const scrollFunction = () => {
    let post = document.getElementById("post");
    if (
      document.body.scrollTop > 670 ||
      document.documentElement.scrollTop > 670
    ) {
      post.style.opacity = "1";
      post.style.pointerEvents = "auto";
    } else {
      post.style.opacity = "0";
      post.style.pointerEvents = "none";
    }
  };

  const handlePostedJob = async (e) => {
    setIsLoad(true);
    e.preventDefault();

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      turnOnOrOffForm();
      navigate('/sign-in');
      return;
    }

    await checkTokenAndRefreshToken();

    const form = e.target;

    const files = form.trackFile.files;
    let trackFile = null;
    if(files.length >0) {
      let formData = new FormData();
      for(let i = 0; i < files.length; i++){
        formData.append('trackFile', files[i]);
      }

      trackFile = await axios.post('http://localhost:8222/api/jobs/posted-job/upload-track', formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          "Authorization": 'Bearer '+localStorage.getItem('access_token')
        }
      })
      .then(response => {
        return response.data;
      })
      .catch(error => {
        console.log(error)
      })
    }
    
    let posterId = null;
    if(localStorage.getItem('user')){
      posterId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      posterId = JSON.parse(sessionStorage.getItem('user')).id;
    }
    const request = {
      posterId: posterId,
      topic: form.topic.value,
      content: form.content.value,
      trackFile: trackFile,
      jobTypeId: form.jobTypeId.value
    } 
    
    await axios.post('http://localhost:8222/api/jobs/posted-job', request, {
      headers: {
        "Authorization": 'Bearer '+localStorage.getItem('access_token')
      }
    })
    .then( response => {
      window.location.reload();
    })
    .catch(error => {
      console.log(error);
    });

    setIsLoad(false);
  }

  const changeFileName =(e) => {
    const files = e.target.files;
    if(files.length>0) {
      for(let i = 0; i < files.length; i++){
        setTrackFileName(files[i].name)
      }
    }
  }

  return (
    <section className={styles.job}>
      <div id="post" className={styles.post}>
        <h1>Connect with top industry professionals</h1>
        {(role == null || role == 'CUSTOMER') && 
          <button onClick={turnOnOrOffForm}>POST YOUR JOB</button>
        }
      </div>
      <div className={`${styles["job-card"]}`}>
        <div className={styles.left}>
          <ul>
              <li>
                <Link onClick={filterGenreJob} value={0} to="#">All</Link>
              </li>
            {jobTypes && jobTypes.map(jobType => (
              <li key={jobType.id}>
                <Link onClick={filterGenreJob} value={jobType.id} to="#">{jobType.name}</Link>
              </li>
            ))}
          </ul>
        </div>
        <div className={styles.right}>
          {
            postedJobs && postedJobs.map(postedJob => (
              <div key={postedJob.id} className={styles.card}>
                <div className={styles.avatar}>
                  <img src={postedJob.poster.image ?
                    'http://localhost:8222/api/customers/images/'+postedJob.poster.image: images.defaultImage} alt="" />
                  <p>{postedJob.poster.user.fullName}</p>
                  <p>{postedJob.postDate}</p>
                </div>
                <div className={styles.info}>
                  <h4>
                    <Link to={`/job-details/${postedJob.id}`}>
                      {postedJob.topic}
                    </Link>
                  </h4>
                  <p>
                    {postedJob.content}
                  </p>
                </div>
              </div>
            ))
          }
        </div>
      </div>
      <Pagination totalJobProp={total} isFilter={isFilter} filteredJobType={filteredJobType} 
        setIsLoad={setIsLoad} setPostedJobs={setPostedJobs} setTotal={setTotal} indexPass={indexPass} />
      {isForm  &&
        <div id="post-job" className={styles['post-job']}>
          <form onSubmit={(e) => handlePostedJob(e)} >
            <div onClick={turnOnOrOffForm} className={styles.out}>
              <span>X</span>
            </div>
            <div className={styles.row}>
              <label htmlFor='topic'>Topic</label>
              <input type="text" name="topic" id="topic" placeholder="Enter topic..." required />
            </div>
            <div className={styles.row}>
              <label htmlFor='content'>Content</label>
              <textarea name="content" id="content" placeholder="Enter content..." required></textarea>
            </div>
            <div className={styles.row}>
              <label htmlFor='trackFile' className={styles['label-file']} >{trackFileName}</label>
              <input type="file" name="trackFile" id="trackFile" accept=".mp3,audio/*" onChange={changeFileName} />
            </div>
            <div className={styles.row}>
              <label htmlFor="jobType">Job Type</label>
              <select size={2} id="jobType" name="jobTypeId" required  >
                {jobTypes && jobTypes.map(jobType => (
                  <option key={jobType.id}  value={jobType.id}>{jobType.name}</option>
                ))}
              </select>
            </div>
            <div className={styles.row}>
              <button type="submit">CONTINUTE</button>
            </div>
          </form>
        </div>
      }
      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </section>
  );
};
export default Job;
