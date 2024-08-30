import { Link, useLocation, useNavigate } from "react-router-dom";
import images from "../../../assets/images";
import styles from "./Header.module.css";
import Hero from "../Hero/Hero";
import Explore from "../Explore/Explore";
import Artist from "../Artist/Artist";
import Job from "../Job/Job";
import Beat from "../Beat/Beat";
import SoundCheck from "../SoundCheck/SoundCheck";
import SoundChecker from "../SoundChecker/SoundChecker";
import Customer from "../Customer/Customer";
import { useContext, useEffect, useRef, useState } from "react";
import axios from "axios";
import { Context } from "../../../common/ContextProvider";
import Favorite from "../Favorite/Favorite";
import Notification from "../../Notification/Notication";

const Header = () => {
  const location = useLocation();
  const currentPath = location.pathname;
  const navigate = useNavigate();

  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const {checkToken, refreshToken, role, checkRole, numberOfNotifications, numberOfMessages} = useContext(Context);
  
  const [hide, setHide] = useState(1);

  useEffect( () => {
    // console.log(localStorage.getItem('access_token'))
    // console.log(localStorage.getItem('refresh_token'))
    // console.log(localStorage.getItem('user'))
    checkRole();

    if(currentPath.includes('/sign-in') || currentPath.includes('/job-detail') || currentPath.includes('/my-jobs') ||
      currentPath.includes('/my-posted-beats') || currentPath.includes('/my-purchased-beats')
      || currentPath.includes('/my-task') || currentPath.includes('/rented-customer-tasks')
      || currentPath.includes('/messages') || currentPath.includes('/sign-up')|| currentPath.includes('/profile')) {
      if(document.getElementById('header') != null){
        document.getElementById('header').style.height = 'auto';
      }
    }else {
      if(document.getElementById('header') != null){
        document.getElementById('header').style.height = '670px';
      }
    }
  }, [currentPath]);

  const toTopPage = () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  window.onscroll = function() {scrollFunction()};

  const scrollFunction = () =>{
    let mybutton = document.getElementById('myButton');
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
      mybutton.style.display = "block";
    } else {
      mybutton.style.display = "none";
    }
  }

  const handleLogout = async (e) => {
    e.preventDefault();

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    if(! await checkToken(localStorage.getItem('access_token'))){
      if(! await checkToken(localStorage.getItem('refresh_token'))){
        localStorage.removeItem('user');
        sessionStorage.removeItem('user');
        navigate('/sign-in');
      }else {
        const tokenResponse = await refreshToken(localStorage.getItem('refresh_token'));
        localStorage.setItem('access_token', tokenResponse.access_token);
      }
    }

    try {
      await axios.post('http://localhost:8222/api/users/logout',{
        refresh_token: localStorage.getItem('refresh_token'),
        client_id: import.meta.env.VITE_CLIENT_ID,
        client_secret: import.meta.env.VITE_CLIENT_SECRET
      }, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('access_token')}`, 
          'Content-Type': 'application/json'
        }
      }
    );
    } catch (error) {
      console.error('Error:', error);
    }

    sessionStorage.removeItem('user');
    localStorage.removeItem('user');

    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');

    navigate('/sign-in');
  }

  const hideNotification = () => {
    setHide(prev => !prev);
  }

    return ( 
      <header id="header">
        <div className={styles.container}>
          <Link to="/">
            <img src={images.logo} alt="logo" />  
          </Link>
          <nav>
            <ul>
              <li>
                <Link to="/explore">
                  Explore
                </Link>
              </li>
              <li>
                <Link to="/jobs">
                  Recent Job
                </Link>
              </li>
              <li>
                <Link to="/sound-checks">
                  SoundCheck
                </Link>
              </li>
              <li>
                <Link to="/beats">
                  Beats
                </Link>
              </li>
              <li>
                {(!sessionStorage.getItem('user') && !localStorage.getItem('user')) && 
                  <Link to="/sign-in">
                  Sign in
                </Link>
                }
              </li>
            </ul>
            <ul className={styles["second-ul"]}>
              {(sessionStorage.getItem('user') || localStorage.getItem('user'))  && role == 'CUSTOMER' && 
                <li>
                  <Link to="/favorites">
                    <img src={images.heart} alt="" />
                  </Link>
                </li>
              }
              {(sessionStorage.getItem('user') || localStorage.getItem('user')) &&
                <li>
                  <Link to="#">
                    <div className={styles.bell}>
                      <img onClick={hideNotification} src={images.bell} alt="" />
                      <span className={styles.number}>{numberOfNotifications != 0 && numberOfNotifications}</span>
                    </div>
                  </Link>
                  <Notification hide={hide} />
                </li>
              }
              {(sessionStorage.getItem('user') || localStorage.getItem('user')) &&
                <li>
                  <Link className={styles.message} to="/messages">
                    <span>
                      <img src={images.message} alt="" />
                    </span>
                    <span>Message</span>
                    <span className={styles.number}>{numberOfMessages !=0 && numberOfMessages}</span>
                  </Link>
                </li>
              }
              <li>
                <div className={styles.dropdown}>
                  <button type="button" className={styles.dropbtn}>My Account</button>
                  <div className={styles['dropdown-content']}>
                    {(sessionStorage.getItem('user') || localStorage.getItem('user')) &&
                      <Link to="/profile">My Profile</Link>
                    }
                    {(sessionStorage.getItem('user') || localStorage.getItem('user'))  && role == 'CUSTOMER' &&
                      <Link to="/my-jobs">My Posted Jobs</Link>
                    }
                    {(sessionStorage.getItem('user') || localStorage.getItem('user'))  && role == 'CUSTOMER' &&
                      <Link to="/my-purchased-beats">My Purchased Beat</Link>
                    }
                    {(sessionStorage.getItem('user') || localStorage.getItem('user'))  && role == 'CUSTOMER' &&
                      <Link to="/rented-customer-tasks">Hired Task</Link>
                    }
                    {(sessionStorage.getItem('user') || localStorage.getItem('user')) && role == 'ARTIST' &&
                     <Link to="/my-posted-beats">My Posted Beats</Link>
                    }
                    {(sessionStorage.getItem('user') || localStorage.getItem('user'))  && role == 'ARTIST' &&
                      <Link to="/my-tasks">My Task</Link>
                    }
                    {(sessionStorage.getItem('user') || localStorage.getItem('user')) 
                      && <Link to="#"  onClick={(e) => handleLogout(e)}>Logout</Link>
                    }
                  </div>
                </div>
              </li>
            </ul>
          </nav>
        </div>
          {currentPath == '/' && <Hero />}
          {currentPath.includes('/explore') && <Explore />}
          {currentPath.includes('/artists') && <Artist id={currentPath.split('/').pop()} />}
          {currentPath.includes('/customers') && <Customer id={currentPath.split('/').pop()} />}
          {currentPath.includes('/jobs') && <Job />}
          {currentPath.includes('/beats') && <Beat />}
          {currentPath.includes('/sound-checks') && <SoundCheck />}
          {currentPath.includes('/sound-checkers') && <SoundChecker id={currentPath.split('/').pop()} />}
          {currentPath.includes('/favorites') && <Favorite />}

        <button onClick={toTopPage} id="myButton" className={styles.myButton} title="Go to top">Top</button>
      </header>
     );
}
 
export default Header;