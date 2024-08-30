import { useContext, useEffect, useState } from "react";
import ReviewContent from "../HomePageContents/ReviewContent/ReviewContent";
import styles from "./SoundChecker.module.css";
import { Link, useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { Context } from "../../common/ContextProvider";

const SoundChecker = () => {
  const [soundChecker, setSoundChecker] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [isLoad, setIsLoad] = useState(true);
  const {id} = useParams();

  const {isForm , turnOnOrOffForm, checkTokenAndRefreshToken, role, checkRole, clientRef} = useContext(Context);

  const [isHideContact, setIsHideContact] = useState(true);

  const navigate = useNavigate();
  
  useEffect(() => {
    setIsLoad(true);

    checkRole();

    const fetchArtistData = async ()=> {
      await axios.get('http://localhost:8222/api/artists/'+id)
      .then(response => {
        setSoundChecker(response.data);
      })
      .catch(error => {
        console.log(error);
      });
    } 

    const fetchReviewsData = async ()=> {
      await axios.get('http://localhost:8222/api/reviews/find-by-expert-id-in-sound-checker-page/'+id)
      .then(response => {
        setReviews(response.data.reviews);
      })
      .catch(error => {
        console.log(error);
      });
    } 

    fetchArtistData();
    fetchReviewsData();

    setIsLoad(false);
  }, []);

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
      
      if(soundChecker && soundChecker.id != null) {
        axios.get('http://localhost:8222/api/conversations/find-conversation-of-two-people/'+idRole+'/'+soundChecker.id+' '+soundChecker.user.role, {
          headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token')
          },
          params: {
            page: 0
          }
        })
        .then(response => {
          console.log(response.data.total)
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
  }, [role, soundChecker]);

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
      setReviews(prev => [response.data, ...prev]);
      setIsLoad(false);
      form.reset();
    })
    .catch(error => {
      console.log(error);
    })
  }

  const handleHireSoundChecker = async (e) => {
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

    axios.post('http://localhost:8222/api/hires/confirm-sound-checker-hire-in-mail-of-sound-checker', {
      hirerId: hirerId,
      expertId: id,
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
        receiverIdRole: soundChecker.id + ' ' + soundChecker.user.role,
        message: userInput
      }
  
      clientRef.current.send('/app/private', {}, JSON.stringify(request));
      window.location.reload();
    }
  }

  return (
    <section className={styles.soundChecker}>
      <div className={`${"container-common"}`}>
        <div className={styles.info}>
          <div className={styles.left}>
            <div className={styles.about}>
              <h3>About Matt {soundChecker && soundChecker.artistName}</h3>
              <p>
                {soundChecker && soundChecker.introduction}
              </p>
            </div>
          </div>
          <div className={styles.right}>
            {(role == null || role == 'CUSTOMER') &&
              <div onClick={turnOnOrOffForm} className={styles.card}>
                <div className={styles.price}>
                  <h3>SOUNDCHECK</h3>
                  <p>
                    One round of voice or written feedback delivered within 5 Days
                  </p>
                </div>
                <div className={styles.cost}>${soundChecker && soundChecker.hireCost}</div>
              </div>
            }
          </div>
        </div>
        <div className={styles.review}>
          {role && role == 'CUSTOMER' &&
            <form className={styles.form} onSubmit={(e) => handleReview(e)} >
              <textarea  name="content" placeholder="Enter your comment..."  required></textarea>
              <button type="submit">Send</button>
            </form>
          }
          {(role) && isHideContact == true && 
            <Link onClick={handleContact}    to="#">
              <button>Contact </button>
            </Link>
          }

          <ReviewContent reviews={reviews} />
        </div>
        <div className={styles.benefit}>
          <p className={styles.title}>What is this SoundCheck product?</p>
          <p>
            Get feedback on your music, production or mix from the industry’s
            best producers, sound engineers and songwriters.
          </p>
          <p>
            Choose a SoundCheck option, upload your file on the checkout page
            along with a note about the kind of feedback you're interested in,
            and get actionable written or voice feedback from an industry pro.
          </p>
          <p>
            Benefit from a renowned professional’s set of ears, improve your
            track before release, learn and get better sounding tracks.
          </p>
          <p>
            Note - you may not use the name, image, or likeness of the
            professional you purchased SoundCheck from as credited personnel or
            otherwise.
          </p>
        </div>
      </div>
      {isForm  &&
        <div id="hire" className={styles.hire}>
          <form onSubmit={(e) => handleHireSoundChecker(e)}  >
            <div onClick={turnOnOrOffForm} className={styles.out}>
              <span>X</span>
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

export default SoundChecker;
