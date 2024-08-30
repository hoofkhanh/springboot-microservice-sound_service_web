import { useState } from "react";
import styles from "./Signup.module.css";
import { Link, useNavigate } from "react-router-dom";
import { useEffect } from "react";
import axios from "axios";

const Signup = () => {
  const [isLoad, setIsLoad] = useState(false);
  const [customerRole, setCustomerRole] = useState(false);
  const [artistRole, setArtistRole] = useState(true);
  const [jobType, setJobType] = useState([]);

  const [soundMakeOrChecker, setSoundMakeOrChecker] = useState('');

  const navigate = useNavigate();

  useEffect(() => {
    setIsLoad(true);
    axios.get('http://localhost:8222/api/jobs/job-type')
    .then(response => {
      setIsLoad(false);
      setJobType(response.data);
    })
    .catch(error => {
      console.log(error);
    })
  }, []);

  const changeArtistRole = () => {
    setCustomerRole(false);
    setArtistRole(true);
  }

  const changeCustomerRole = () => {
    setCustomerRole(true);
    setArtistRole(false);
  }

  const changeSoundMaker = () => {
    setSoundMakeOrChecker('SOUND_MAKER');
  }

  const changeSoundChecker = () => {
    setSoundMakeOrChecker('SOUND_CHECKER');
  }

  const handleCreateUser = async (e) => {
    e.preventDefault();

    setIsLoad(true);

    let token = null;
    await axios.post('http://localhost:8222/api/users/get-token-no-need-login', {
      client_id : import.meta.env.VITE_CLIENT_ID,
      client_secret : import.meta.env.VITE_CLIENT_SECRET,
      grant_type: 'client_credentials'
    })
    .then(response => {
      token = response.data.access_token;
    })
    .catch(error => {
      console.log(error);
    })

    const form = e.target;

    const formData = new FormData();

    if(artistRole){
      let result = [];
      let options = form.jobTypeIds && form.jobTypeIds.options;
      let opt;
      for (let i=0, iLen=options.length; i<iLen; i++) {
        opt = options[i];
        if (opt.selected) {
          result.push(opt.value || opt.text);
        }
      }
  
      const request = {
        artistName: form.artistName.value,
        introduction: form.introduction.value,
        nameOfIntroductionTrack: form.nameOfIntroductionTrack.value,
        artistType: soundMakeOrChecker,
        hireCost: form.hireCost.value,
        user : {
          email: form.email.value,
          password: form.password.value,
          fullName: form.fullName.value,
          phoneNumber: form.phoneNumber.value,
          country: form.country.value,
          dayOfBirth: form.dateOfBirth.value,
          role: 'ARTIST'
        },
        jobTypeIds: result
      }
  
      const imageFile = form.image.files;
      let image = null;
      if (imageFile.length > 0) {
        image = imageFile[0];
      }else {
        alert('Cần có hình');
        return;
      }
  
      const trackFile = form.trackFile.files;
      let track = null;
      if (trackFile.length > 0) {
        track = trackFile[0];
      }else {
        alert('cần có track');
        return;
      }
  
      formData.append('request',new Blob([JSON.stringify(request)], { type: 'application/json' }))
      formData.append('imageFile', image)
      formData.append('trackFile', track)
      formData.append('token', token);
  
      axios.post('http://localhost:8222/api/artists', formData)
      .then(response => {
        setIsLoad(false);
        navigate('/sign-in');
      })
      .catch(error => {
        alert(error.response.data)
        console.log(error);
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
      }else {
        alert('cần thêm hình')
        return;
      }

      const request = {
        image: image,
        user : {
          email: form.email.value,
          password: form.password.value,
          fullName: form.fullName.value,
          phoneNumber: form.phoneNumber.value,
          country: form.country.value,
          dayOfBirth: form.dateOfBirth.value,
          role: 'CUSTOMER'
        },
        token: token
      }

      axios.post('http://localhost:8222/api/customers', request)
      .then(response => {
        setIsLoad(false);
        navigate('/sign-in');
      })
      .catch(error => {
        alert(error.response.data)
        console.log(error);
      })
    }
    
  }

  return ( 
    <section className={styles.signup}>
      <div className={styles.container}>
        <div className={styles.form}>
        
          <div className={styles['signup-form']}>
            <form onSubmit={(e) => handleCreateUser(e)}>
              <div className={styles.files}>
                <label className={styles.labelFile} htmlFor="image">Image</label>
                <input id="image" name="image" type="file" placeholder="Email"  />
              </div>
              <div><input name="email" type="email" placeholder="Email" required /></div>
              <div><input name="password" type="password" placeholder="Password" required /></div>
              <div><input name="fullName" type="text" placeholder="Full Name" required /></div>
              <div><input name="phoneNumber" type="text" placeholder="Phone Number" required /></div>
              <div><input name="country" type="text" placeholder="Country" required /></div>
              <div><input name="dateOfBirth" type="date" placeholder="Day Of Birth" required /></div>
              <div className={styles.radio}>
                <label htmlFor="artist">ARTIST</label>
                <div>
                  <input onChange={changeArtistRole} id="artist" name="role" type="radio" required />
                </div>
              </div>
              <div className={styles.radio}>
                <label htmlFor="customer">CUSTOMER</label>
                <div>
                 <input onChange={changeCustomerRole} id="customer" name="role" type="radio" required/>
                </div>
              </div>
              {artistRole && <div><input name="artistName" type="text" placeholder="Artist Name" required /></div>}
              {artistRole && <div><textarea name="introduction" type="text" placeholder="Introduction" required ></textarea></div>}
              {artistRole && 
                <div><input name="nameOfIntroductionTrack" type="text" placeholder="Name of introduction track" required /></div>}
              {artistRole && 
                <div className={styles.files}>
                  <label className={styles.labelFile} htmlFor="trackFile">Introduction track file</label>
                  <input id="trackFile" name="trackFile" type="file"  />
                </div>
              }
              {artistRole && <div><input name="hireCost" type="text" placeholder="Hire Cost" required /></div>}
              {artistRole &&
                <div className={styles.radio}>
                  <label htmlFor="soundMaker">SOUND MAKER</label>
                  <div>
                    <input  onChange={changeSoundMaker} id="soundMaker" name="artistType" type="radio" required />
                  </div>
                </div>
              }
              {artistRole &&
                <div className={styles.radio}>
                  <label htmlFor="soundChecker">SOUND CHECKER</label>
                  <div>
                    <input onChange={changeSoundChecker} id="soundChecker" name="artistType" type="radio" required/>
                  </div>
                </div>
              }
              {artistRole &&
                <div >
                  <select name="jobTypeIds" multiple>
                    {jobType && jobType.map(job => (
                      <option key={job.id} value={job.id}>{job.name}</option>
                    ))}
                  </select>
                </div>
              }
              
              
              <button type="submit">
                SIGN UP
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
 
export default Signup;