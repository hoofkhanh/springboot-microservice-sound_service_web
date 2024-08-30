import { useContext, useEffect, useState } from "react";
import styles from "./MyJob.module.css";
import axios from "axios";
import { Context } from "../../../common/ContextProvider";
import { useNavigate } from "react-router-dom";

const MyJob = () => {
  const [myJobs, setMyJobs]= useState([]);
  const [jobTypes, setJobTypes] = useState([]);
  const [posterId, setPosterId] = useState(null);

  const { isForm , turnOnOrOffForm, checkTokenAndRefreshToken} = useContext(Context);
  const [isLoad, setIsLoad] = useState(true);
  const [trackFileName, setTrackFileName] = useState('Track File (Optional)');

  const [formData, setFormData] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }
    
    let posterId = null;
    if(localStorage.getItem('user')){
      posterId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      posterId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    if(!posterId) {
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    setPosterId(posterId);
    const fetchMyJobs = async() => { 
      await axios.get('http://localhost:8222/api/jobs/posted-job/find-by-poster-id/'+posterId)
      .then(response => {
        setMyJobs(response.data);
      })
      .catch(error => {
        console.log(error);
      });
    }
    const fetchAllGenreJob = async () => {
      await axios.get('http://localhost:8222/api/jobs/job-type')
      .then(async response => {
        setJobTypes(response.data);
      })
      .catch(error => {
        console.log(error);
      });
    }

    fetchMyJobs();
    fetchAllGenreJob();
    setIsLoad(false);

  }, []);

  useEffect(() => {
    setTrackFileName('Track File (Optional)');
  }, [isForm]);

  const handleTurnOnUpdate = (job) => {
    setFormData({
      id: job.id,
      topic : job.topic,
      content: job.content,
      jobTypeId: job.jobType.id,
      trackFile: job.trackFile
    })
    turnOnOrOffForm();
  }

  const changeFileName =(e) => {
    const files = e.target.files;
    if(files.length>0) {
      for(let i = 0; i < files.length; i++){
        setFormData(prev => ({
          ...prev,
          trackFile: files[i].name
        }))
      }
    }
  }

  const handleUpdateJob = async (e) => {
    setIsLoad(true);
    e.preventDefault();

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
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
    }else {
      trackFile = formData.trackFile;
    }
    
    let posterId = null;
    if(localStorage.getItem('user')){
      posterId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      posterId = JSON.parse(sessionStorage.getItem('user')).id;
    }
    const request = {
      id: formData.id,
      posterId: posterId,
      topic: form.topic.value,
      content: form.content.value,
      trackFile: trackFile,
      jobTypeId: form.jobTypeId.value
    } 
    
    await axios.put('http://localhost:8222/api/jobs/posted-job', request, {
      headers: {
        "Authorization": 'Bearer '+localStorage.getItem('access_token')
      }
    })
    .then(async response => {
      window.location.reload();
    })
    .catch(error => {
      console.log(error);
    });

    setIsLoad(false);
  }

  const handleDeleteMyJob = async (id) => {
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    await checkTokenAndRefreshToken();

    await axios.delete('http://localhost:8222/api/jobs/posted-job/'+id, {
      headers: {
        'Authorization' : 'Bearer '+ localStorage.getItem('access_token')
      }
    })
    .catch(error => {
      console.log(error);
    });

    window.location.reload();

    setIsLoad(false);
  }

  return ( 
    <section className={styles.myjob}>
      <div className="container-common">
        <div>
          <h1>My Posted Jobs</h1>
          <div className={styles.table}>
            {myJobs && myJobs.map((job,index) => (
              <div key={job.id} className={styles.card}>
                <p className={styles.remove} onClick={() => handleDeleteMyJob(job.id)}>X</p>
                <button onClick={() => handleTurnOnUpdate(job)}  className={styles.update}>Update</button>
                <p className={styles.index}>{index +1}</p>
                <p className={styles.topic}>{job.topic}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
      {isForm  &&
        <div id="update-job" className={styles['update-job']}>
          <form onSubmit={handleUpdateJob} >
            <div onClick={turnOnOrOffForm} className={styles.out}>
              <span>X</span>
            </div>
            <div className={styles.row}>
              <label htmlFor='topic'>Topic</label>
              <input value={formData && formData.topic} onChange={(e) => setFormData(prev => ({...prev, topic: e.target.value}))} 
                type="text" name="topic" id="topic" placeholder="Enter topic..." required />
            </div>
            <div className={styles.row}>
              <label htmlFor='content'>Content</label>
              <textarea value={formData && formData.content} onChange={(e) => setFormData(prev => ({...prev, content: e.target.value}))} 
               name="content" id="content" placeholder="Enter content..." required></textarea>
            </div>
            <div className={styles.row}>
              <label htmlFor='trackFile' className={styles['label-file']} >
                {formData && formData.trackFile?formData.trackFile:'Track File (Optional)'}
              </label>
              <input type="file" name="trackFile" id="trackFile" accept=".mp3,audio/*" onChange={changeFileName} />
            </div>
            <div className={styles.row}>
              <label htmlFor="jobType">Job Type</label>
              <select value={formData && formData.jobTypeId} onChange={(e) => setFormData(prev => ({...prev, jobTypeId: e.target.value}))} 
               size={2} id="jobType" name="jobTypeId" required  >
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
}
 
export default MyJob;