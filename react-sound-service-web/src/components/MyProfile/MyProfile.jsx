import { useContext, useEffect, useState } from "react";
import styles from "./MyProfile.module.css";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { Context } from "../../common/ContextProvider";

const MyProfile = () => {
  const [isLoad, setIsLoad] = useState(false);

  const [artistRole, setArtistRole] = useState(null);

  const [object, setObject] = useState(null);

  const navigate = useNavigate();

  const [jobType, setJobType] = useState([]);

  const { checkTokenAndRefreshToken, role, checkRole} = useContext(Context);

  useEffect(() => {
    

    setIsLoad(true);
    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    checkRole();

    axios.get('http://localhost:8222/api/jobs/job-type')
    .then(response => {
      setIsLoad(false);
      setJobType(response.data);
    })
    .catch(error => {
      console.log(error);
    })

    if(role) {
      let id = null;
      if(localStorage.getItem('user')){
        id = JSON.parse(localStorage.getItem('user')).id;
      }

      if(sessionStorage.getItem('user')){
        id = JSON.parse(sessionStorage.getItem('user')).id;
      }

      if(role == 'ARTIST') {
        axios.get('http://localhost:8222/api/artists/'+id)
        .then(response => {
          const updatedData = {
            ...response.data,
            jobTypeIds: response.data.skills.map(jobType => jobType.jobTypeId) // Rename and transform
          };

          delete updatedData.skills;

          setObject(updatedData);

          setIsLoad(false);
         
        })
        .catch(error => {
          console.log(error);
        })
      }else {
        axios.get('http://localhost:8222/api/customers/'+id)
        .then(response => {
          setObject(response.data);
        })
        .catch(error => {
          console.log(error);
        })
      }
    }

  }, [role]);

  const handleSelectChange = (e) => {
    const selectedOptions = Array.from(e.target.selectedOptions, option => parseInt(option.value));

    // Update the object with the new job type selections
    setObject(prev => ({
      ...prev,
      jobTypeIds: [...new Set([...prev.jobTypeIds, ...selectedOptions])]
    }));

  };

  const handleUpdate = async (e) => {
    setIsLoad(false);
    e.preventDefault();

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    const form = e.target;

    if(role == 'ARTIST'){
      const imageFile = form.image.files;
      let image = null;
      if (imageFile.length > 0) {
        image = imageFile[0];
      }

      const trackFile = form.trackFile.files;
      let track = null;
      if (trackFile.length > 0) {
        track = trackFile[0];
      }

      const formData = new FormData();

      formData.append('request',new Blob([JSON.stringify(object)], { type: 'application/json' }))
      formData.append('imageFile', image?image:new Blob([]))
      formData.append('trackFile', track?track:new Blob([]))

      axios.put('http://localhost:8222/api/artists', formData, {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      })
      .then(response => {
        window.location.reload();
        setIsLoad(true);
      })
      .catch(error => {
        console.log(error);
        alert(error.response.data);
      })
    }else {
      const imageFile = form.image.files;
      let image = null;
      if (imageFile.length > 0) {
        let formData = new FormData();
        formData.append('imageFile', imageFile[0]);

        image = await axios.post('http://localhost:8222/api/customers/upload-image-file', formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          }
        })
        .then(response => {
          return response.data;
        })
        .catch(error => {
          console.log(error)
        })
      }

      let request = object;

      if(image) {
        request.image = image;
      }

      console.log(request)

      axios.put('http://localhost:8222/api/customers', request, {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      })
      .then(response => {
        window.location.reload();
        setIsLoad(true);
      })
      .catch(error => {
        console.log(error);
        alert(error.response.data);
      })
    }
  }

  return (
    <section className={styles.signup}>
      <div className={styles.container}>
        <div className={styles.form}>
        
          <div className={styles['signup-form']}>
            <form onSubmit={(e) => handleUpdate(e)} >
              <div className={styles.files}>
                <label className={styles.labelFile} htmlFor="image">Image</label>
                <input id="image" name="image" type="file"   />
              </div>
              <div><input value={object ? object.user.fullName: ''} onChange={(e) => setObject(prev => ({ ...prev, user: { ...prev.user, fullName: e.target.value, } }))} name="fullName" type="text" placeholder="Full Name" required /></div>
              <div><input value={object ? object.user.phoneNumber: ''} onChange={(e) => setObject(prev => ({ ...prev, user: { ...prev.user, phoneNumber: e.target.value, } }))} name="phoneNumber" type="text" placeholder="Phone Number" required /></div>
              <div><input value={object ? object.user.country: ''} onChange={(e) => setObject(prev => ({ ...prev, user: { ...prev.user, country: e.target.value, } }))} name="country" type="text" placeholder="Country" required /></div>
              <div><input value={object ? object.user.dayOfBirth: ''} onChange={(e) => setObject(prev => ({ ...prev, user: { ...prev.user, dayOfBirth: e.target.value, } }))} name="dateOfBirth" type="date" placeholder="Day Of Birth" required /></div>
              {role && role == 'ARTIST' && <div><input value={object ? object.artistName: ''} onChange={(e) => setObject(prev => ({ ...prev, artistName: e.target.value }))} name="artistName" type="text" placeholder="Artist Name" required /></div>}
              {role && role == 'ARTIST' && <div><textarea value={object ? object.introduction: ''} onChange={(e) => setObject(prev => ({ ...prev, introduction: e.target.value }))}  name="introduction" type="text" placeholder="Introduction" required ></textarea></div>}
              {role && role == 'ARTIST' && 
                <div><input  value={object ? object.nameOfIntroductionTrack: ''} onChange={(e) => setObject(prev => ({ ...prev, nameOfIntroductionTrack: e.target.value }))} name="nameOfIntroductionTrack" type="text" placeholder="Name of introduction track" required /></div>}
              {role && role == 'ARTIST' && 
                <div className={styles.files}>
                  <label className={styles.labelFile} htmlFor="trackFile">Introduction track file</label>
                  <input id="trackFile" name="trackFile" type="file"  />
                </div>
              }
              {role && role == 'ARTIST' && <div><input value={object ? object.hireCost: ''} onChange={(e) => setObject(prev => ({ ...prev, hireCost: e.target.value }))} name="hireCost" type="text" placeholder="Hire Cost" required /></div>}
              {role && role == 'ARTIST' &&
                <div >
                  <select name="jobTypeIds" value={object ? object.jobTypeIds: []} onChange={handleSelectChange} multiple>
                    {jobType && jobType.map(job => (
                      <option  key={job.id} value={job.id}>{job.name}</option>
                    ))}
                  </select>
                </div>
              }
              <button type="submit">
                Change
              </button>
            </form>
          </div>
          <div className={styles.otherOption}>
            <p>Not a member? <Link to='/sign-in'>Sign in</Link></p>
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
 
export default MyProfile;