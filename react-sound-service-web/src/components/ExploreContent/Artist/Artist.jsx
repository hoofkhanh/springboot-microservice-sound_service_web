import styles from "./Artist.module.css";
import images from "../../../assets/images";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useContext, useEffect, useRef, useState } from "react";
import axios from "axios";
import WaveSurfer from 'https://cdn.jsdelivr.net/npm/wavesurfer.js@7/dist/wavesurfer.esm.js';
import audio from "../../../assets/Ảo Giác.mp3";
import { Context } from "../../../common/ContextProvider";

const Artist = () => {
  const [buttonPlayOrPause, setButton] = useState(images.play);

  const wavesurfer = useRef(null);
  const waveformRef = useRef();

  const [isLoad, setIsLoad] = useState(true);
  const {id} = useParams();
  const [artist, setArtist] = useState({});

  const [reviews, setReviews] = useState([]);

  const [total, setTotal] = useState(0);

  const [isFavorite, setIsFavorite] = useState(false);
  const [favoriteId, setFavoriteId] = useState(false);

  const { checkTokenAndRefreshToken, role, checkRole, clientRef, isForm, turnOnOrOffForm} = useContext(Context);

  const [isHideContact, setIsHideContact] = useState(true);

  const navigate = useNavigate();

  useEffect( () => {
    setIsLoad(true);

    checkRole();

    const fetchFavorite = async () => {
      if((localStorage.getItem('user') || sessionStorage.getItem('user')) && role == 'CUSTOMER' ) {
        const checkTokenAndRefreshTokenFunction = async () => {
          await checkTokenAndRefreshToken();
        }
  
        const fetchDataFavorite = async () => {
          let accountOwnerId = null;
          if(localStorage.getItem('user')){
            accountOwnerId = JSON.parse(localStorage.getItem('user')).id;
          }
  
          if(sessionStorage.getItem('user')){
            accountOwnerId = JSON.parse(sessionStorage.getItem('user')).id;
          }
  
          await axios.get('http://localhost:8222/api/favorites/favorite-artist/find-by-account-owner-id/'+accountOwnerId, {
            headers: {
              'Authorization': 'Bearer '+ localStorage.getItem('access_token')
            }
          })
          .then(response => {
            const isFavorite = response.data.some(item => item.artist.id === id);
            setIsFavorite(isFavorite);

            const favoriteId = response.data.filter(row => row.artist.id == id)[0];
            setFavoriteId(favoriteId ? favoriteId.id: 0);

          })
          .catch(error => {
            console.log(error);
          })
        }
  
        await checkTokenAndRefreshTokenFunction();
  
        await fetchDataFavorite();
      }
    }

    fetchFavorite();

     axios.get(`http://localhost:8222/api/artists/${id}`)
    .then(response => {
      setArtist(response.data);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });

    

  },[role]);

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
      
      if(artist && artist.id != null) {
        axios.get('http://localhost:8222/api/conversations/find-conversation-of-two-people/'+idRole+'/'+artist.id+' '+artist.user.role, {
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
  }, [role, artist]);

  useEffect( () => {
    setIsLoad(true);

     axios.get(`http://localhost:8222/api/reviews/find-by-expert-id/${id}`, {
      params: {
        page: 0
      }
     })
    .then(response => {
      setReviews(response.data.reviews);
      setTotal(response.data.total);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  },[]);

  useEffect(() => {
    setIsLoad(true);

    const audio = 'http://localhost:8222/api/artists/tracks/' + artist.introductionTrackFile;

    if(waveformRef.current && audio) {
      wavesurfer.current  = WaveSurfer.create({
        container: waveformRef.current,
        waveColor: 'gray',
        progressColor: 'rgb(73, 184, 186)',
        url: audio,
        height: 60,
        width: "auto",
        dragToSeek: true,
        hideScrollbar: true,
        normalize: true,
        barGap: 1,
        height: 60,
        barHeight: 20,
        barRadius: 20,
        barWidth: 5,
      });
    }

    setIsLoad(false);

    return () => {
      if (wavesurfer.current) {
        wavesurfer.current.destroy();
      }
    };

  }, [artist]);

  const playOrPauseMusic = () => {
    if (buttonPlayOrPause != images.pause) {
      setButton(images.pause);
      wavesurfer.current.play();
    } else {
      setButton(images.play);
      wavesurfer.current.pause();
    }
  };

  window.onscroll = function() {scrollFunction()};

  const scrollFunction = () =>{
    let contact = document.getElementById('contact');
    if (document.body.scrollTop > 670 || document.documentElement.scrollTop > 670) {
      contact.style.position = "fixed";
    } else {
      contact.style.position = "relative";
    }
  }

  const handleLoadMore = () => {
    setIsLoad(true);

     axios.get(`http://localhost:8222/api/reviews/find-by-expert-id/${id}`, {
      params: {
        page: Math.floor(reviews.length / 1)
      }
     })
    .then(response => {
      setReviews(prevReviews => [...prevReviews, ...response.data.reviews]);
      setTotal(response.data.total);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  }

  const handleAddOrDeleteFavoriteArtist = async (artistId, favoriteId) => {
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }
    
    await checkTokenAndRefreshToken();

    let accountOwnerId = null;
    if(localStorage.getItem('user')){
      accountOwnerId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      accountOwnerId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    if(isFavorite){
      await axios.delete('http://localhost:8222/api/favorites/favorite-artist/'+favoriteId, {
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('access_token')
        }
      })
      .then(() => {
        setIsFavorite(prev => !prev);
        setFavoriteId(0);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }else {
      await axios.post('http://localhost:8222/api/favorites/favorite-artist', {
        accountOwnerId: accountOwnerId,
        artistId: artistId
      } ,
      {
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setIsFavorite(prev => !prev);
        setFavoriteId(response.data.id);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }

  }

  const handleReview = async (e) => {
    e.preventDefault();
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }
    
    await checkTokenAndRefreshToken();

    let reviewerId = null;
    if(localStorage.getItem('user')){
      reviewerId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      reviewerId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    const form = e.target;

    axios.post('http://localhost:8222/api/reviews', {
      reviewerId: reviewerId,
      expertId: id,
      content: form.content.value
    }, {
      headers: {
        Authorization: 'Bearer '+ localStorage.getItem('access_token')
      }
    })
    .then(response => {
      setReviews([response.data]);
      setIsLoad(false);
      form.reset();
    })
    .catch(error => {
      console.log(error);
    })
  }

  const handleHireSoundMaker = async (e) => {
    e.preventDefault();
    setIsLoad(true);

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      turnOnOrOffForm();
      navigate('/sign-in');
      return;
    }
    
    await checkTokenAndRefreshToken();

    let hirerId = null;
    if(localStorage.getItem('user')){
      hirerId = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      hirerId = JSON.parse(sessionStorage.getItem('user')).id;
    }

    const form = e.target;

    let startDateCheck = form.startDate.value.split('-');
    if(startDateCheck[0] < 2000 || startDateCheck[0].length !=4 || startDateCheck[1].length !=2 || startDateCheck[2].length !=2) {
      alert('Start Date is not valid');
      setIsLoad(false);
      return;
    }

    let endDateCheck = form.endDate.value.split('-');
    if(endDateCheck[0] < 2000 || endDateCheck[0].length !=4 || endDateCheck[1].length !=2 || endDateCheck[2].length !=2) {
      alert('End Date is not valid');
      setIsLoad(false);
      return;
    }

    if(new Date(form.startDate.value).getTime() <= new Date().getTime()){
      alert('Start Date is not valid');
      setIsLoad(false);
      return;
    }

    if(new Date(form.endDate.value).getTime() <= new Date().getTime()){
      alert('End Date is not valid');
      setIsLoad(false);
      return;
    }

    if(new Date(form.startDate.value).getTime() >= new Date(form.endDate.value).getTime() ) {
      alert('End Date must > Start Date');
      setIsLoad(false);
      return ;
    }

    axios.post('http://localhost:8222/api/hires/confirm-sound-maker-hire-in-mail-of-sound-maker', {
      hirerId: hirerId,
      expertId: id,
      jobTypeId: form.jobTypeId.value,
      startDate: form.startDate.value,
      endDate: form.endDate.value
    }, {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('access_token')
      }
    })
    .then(() => {
      alert('Pls, check your mail and wati expert confirm this request, then you pay for that !');
      form.reset();
      turnOnOrOffForm();
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    })

  }

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
        receiverIdRole: artist.id + ' ' + artist.user.role,
        message: userInput
      }
  
      clientRef.current.send('/app/private', {}, JSON.stringify(request));
      window.location.reload();
    }
  }

  return (
    <section className={styles.artist}>
      <div id="contact" className={styles.contact}>
        {
          role && role == 'CUSTOMER' &&
          <div className={styles.favorite} onClick={() => handleAddOrDeleteFavoriteArtist(artist.id, favoriteId)}>
            {isFavorite ? <img src={images.heartLike} alt="" /> : <img src={images.heart} alt="" />}
            <span>Save to favorites</span>
          </div> 
        }
        
        {(role== null || role == 'CUSTOMER') && 
          <Link onClick={turnOnOrOffForm} className={styles["contact-button"]} to="#">
            <span>Hire </span>
            <span>{artist.artistName}</span>
          </Link>
        }
        {(role) && isHideContact == true && 
          <Link onClick={handleContact} className={styles["contact-button"]} to="#">
            <span>Contact </span>
            <span>{artist.artistName}</span>
          </Link>
        }
      </div>
      <div
        className={`${"container-common"} ${styles["container-artist-info"]}`}
      >
        <div className={styles.left}>
          <div className={styles.introduction}>
            <p>
              {artist.introduction}
            </p>
          </div>
          <div className={styles.reviews}>
            <h1>{total && total} Reviews</h1>
            {role && role == 'CUSTOMER' &&
              <form className={styles.form} onSubmit={(e) => handleReview(e)} >
                <textarea  name="content" placeholder="Enter your comment..."  required></textarea>
                <button type="submit">Send</button>
              </form>
            }
            { reviews && reviews.map(review => (
              <div key={review.id} className={styles['review-card']}>
                <div className={styles['review-top']}>
                  <div className={styles.avatar}>
                    <img src={review.reviewer.image != null ? 
                      'http://localhost:8222/api/customers/images/'+review.reviewer.image: 
                      images.defaultImage} alt="" />
                  </div>
                  <div className={styles['avatar-right']}>
                    <div className={styles.stars}>
                      <img src={images.star} alt="" />
                      <img src={images.star} alt="" />
                      <img src={images.star} alt="" />
                      <img src={images.star} alt="" />
                      <img src={images.star} alt="" />
                    </div>
                    <div>
                      {review.date} <Link to={`/customers/${review.reviewer.id}`}>{review.reviewer.user.fullName}</Link>
                    </div>
                  </div>
                </div>
                <div  className={styles['review-bottom']}>
                  <p>
                    {review.content}
                  </p>
                </div>
              </div>
            ))}
            
            {reviews && reviews.length < total && 
              <div onClick={handleLoadMore} className="load">
                <a>Load More</a>
              </div>
            }
          </div>
        </div>
        <div className={styles.right}>
          <div className={styles.card}>
            <div className={styles["container-music"]}>
              <div onClick={playOrPauseMusic} className={styles["button-play"]}>
                <img src={buttonPlayOrPause} alt="" />
              </div>
            </div>
            <div ref={waveformRef}></div>
            <h4>{artist.nameOfIntroductionTrack}</h4>
          </div>
          <div className={styles.card}>
            {artist.skills &&  artist.skills.map(skill => (
              <div key={skill.jobTypeId} className={styles.skill}>
                <p>{skill.name}</p>
                <p className={styles.price}>{artist.hireCost}$</p>
              </div>
            ))} 
          </div>
          <div className={styles.card}>
            <h4 className={styles.title}>TERMS OF SERVICE</h4>
            <p className={styles.content}>
              Turnaround time is 3 business days per round of revisions. 3
              rounds of revisions are included in the price of the service, each
              revision thereafter is $100 USD.
            </p>
            <p className={styles.content}> 
              No refunds after work has begun.
            </p>
          </div>
        </div>
      </div>
      {isForm  &&
        <div id="hire" className={styles.hire}>
          <form onSubmit={(e) => handleHireSoundMaker(e)} >
            <div onClick={turnOnOrOffForm} className={styles.out}>
              <span>X</span>
            </div>
            <div className={styles.row}>
              <label htmlFor="jobType">Job Type</label>
              <select size={2} id="jobType" name="jobTypeId" required  >
                {artist && artist.skills &&  artist.skills.map(jobType => (
                  <option key={jobType.jobTypeId}  value={jobType.jobTypeId}>{jobType.name}</option>
                ))}
              </select>
            </div>
            <div className={styles.row}>
              <label htmlFor='startDate'>Start Date</label>
              <input type="date" name="startDate" id="startDate" required />
            </div>
            <div className={styles.row}>
              <label htmlFor='endDate'>End Date</label>
              <input type="date" name="endDate" id="endDate" required />
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

export default Artist;
