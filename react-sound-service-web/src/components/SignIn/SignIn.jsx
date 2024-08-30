import ReCAPTCHA from "react-google-recaptcha";
import styles from "./SignIn.module.css";
import { Link, useNavigate } from "react-router-dom";
import images from "../../assets/images";
import { useContext, useEffect, useRef, useState } from "react";
import axios from "axios";
import { Context } from "../../common/ContextProvider";

const SignIn = () => {
  const recaptcha = useRef();
  const [isLoad, setIsLoad] = useState(false);
  const navigate = useNavigate();
  const [isChecked, setIsChecked] = useState(false);

  const {generateNewToken, checkToken} = useContext(Context);

  useEffect(() => {
    const userSession = sessionStorage.getItem('user');
    const userLocal = localStorage.getItem('user');
    if(userSession) {
      navigate('/');
    }

    if(userLocal) {
      navigate('/');
    }
  }, []);

  const login = async (e) => {
    setIsLoad(true);
    e.preventDefault();
    // const recaptchaValue = recaptcha.current.getValue();

    if(1){
      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;
      
      let user = null;
      await axios.post('http://localhost:8222/api/users/login', {
        email: email,
        password: password,
      })
        .then(response => {
          user = response.data;
        })
        .catch(error => {
          console.log(error)
        });

      if(!user){
        alert('Sai tài khoản hoặc mật khẩu');
      }else {
        if(user.role == 'ARTIST') {
          await axios.get('http://localhost:8222/api/artists/find-by-user-id/'+user.id)
            .then(response => {
              if(isChecked) {
                localStorage.setItem('user', JSON.stringify(response.data));
              }else {
                sessionStorage.setItem('user', JSON.stringify(response.data));
              }
            })
            .catch(error => {
              console.log(error)
            });
        }else {
          await axios.get('http://localhost:8222/api/customers/find-by-user-id/'+user.id)
            .then(response => {
              if(isChecked) {
                localStorage.setItem('user', JSON.stringify(response.data));
              }else {
                sessionStorage.setItem('user', JSON.stringify(response.data));
              }
            })
            .catch(error => {
              console.log(error)
            });
        }

        if(!localStorage.getItem('refresh_token')){
          const tokenResponse = await generateNewToken(email, password);
          localStorage.setItem('access_token', tokenResponse.access_token);
          localStorage.setItem('refresh_token', tokenResponse.refresh_token);
        }else {
          const active = await checkToken(localStorage.getItem('refresh_token'));
          if(!active) {
            const tokenResponse = await generateNewToken(email, password);
            localStorage.setItem('access_token', tokenResponse.access_token);
            localStorage.setItem('refresh_token', tokenResponse.refresh_token);
          }
        }
        
        navigate('/');
      }
    }else {
      alert('reCAPTCHA validation failed!');
    }

    setIsLoad(false);
  }

  const handleChecked = () => {
    if(isChecked) {
      setIsChecked(false);
    }else {
      setIsChecked(true);
    }
  }

  return ( 
    <section className={styles.signin}>
      <div className={styles.container}>
        <div className={styles.form}>
          <div className={styles['login-option']}>
            <div className={styles.spotify}>
              <Link to='#'>
                <img src={images.spotify} alt="" />
                <span>SIGN IN WITH SPOTIFY</span>
              </Link>
            </div>
            <div className={styles.facebook}>
              <Link to='#'>
                <img src={images.facebook} alt="" />
                <span>SIGN IN WITH FACEBOOK</span>
              </Link>
            </div>
          </div>
          <div className={styles['login-form']}>
            <form onSubmit={(e) => login(e)}>
              <div><input id="email" type="email" placeholder="Email" required /></div>
              <div><input id="password" type="password" placeholder="Password" required /></div>
              {/* <ReCAPTCHA ref={recaptcha} className={styles.captcha} sitekey={import.meta.env.VITE_APP_SITE_KEY} /> */}
              <div className={styles.rememberme}>
                <div>
                  <label htmlFor="checkbox">Remember me</label>
                  <input type="checkbox" id="checkbox" onChange={handleChecked} className={styles.checkbox} />
                </div>
              </div>
              <button type="submit">
                SIGN IN WITH EMAIL
              </button>
            </form>
          </div>
          <div className={styles.otherOption}>
            <p>Not a member? <Link to='/sign-up'>Sign up</Link></p>
            <p><Link to='#'>Forgot your password</Link></p>
            <p><Link to='#'>Change your password</Link></p>
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

export default SignIn;