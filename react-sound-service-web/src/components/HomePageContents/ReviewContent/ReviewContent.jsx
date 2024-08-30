import styles from "./ReviewContent.module.css";
import images from "../../../assets/images";
import { useState } from "react";
import { useEffect } from "react";

const ReviewContent = ({reviews}) => {
  const [reviewInfo, setReviewInfo] = useState(
    [
      {
        content: "“This is how engineers are found now”", image: images.avatar1, 
        reviewer: {
          user: {
            fullName: "Ed Cherney - Grammy winning producer and mixer of The Rolling Stones, Iggy Pop, Bob Dylan, Eric Clapton"
          }
        }
      },
      {
        content: "“This was my first time hiring a music professional.”", image: images.avatar2, 
        reviewer: {
          user: {
            fullName: "Alexandra N"
          }
        }
      },
      {
        content: "“@SoundBetterNow has really been a come up for me.”", image: images.avatar3, 
        reviewer: {
          user: {
            fullName: "Brian McKnight Jr"
          }
        }
      },
      {
        content: "“I was able to hand over a song and get back magic. That's unbelievable in my book”", image: images.avatar4, 
        reviewer: {
          user: {
            fullName: "RJ"
          }
        }
      }
    ]
  );

  const [index, setIndex] = useState(0);
  const [reviewDisplay, setReviewDisplay] = useState({});

  useEffect(() =>  {
    setReviewDisplay( reviews && reviews.length >0 ? reviews[index]: reviewInfo[index]);
  }, [index, reviews]);
  
  const nextSlide = () => {
    if(reviews && reviews.length >0 && index == reviews.length -1){
      setIndex(0);
    }else if((reviews && reviews.length <=0 && index == reviewInfo.length -1) || (!reviews && index == reviewInfo.length -1)) {
      setIndex(0);
    }else {
      setIndex(prev => prev +1);
    }
  }

  const prevSlide = () => {
    if(reviews && reviews.length >0 && index == 0) {
      setIndex(reviews.length-1);
    }else if ((reviews && reviews.length <=0 && index ==0) || (!reviews &&  index ==0)) {
      setIndex(reviewInfo.length-1);
    }else {
      setIndex(prev => prev -1);
    }
  }

  return ( 
    <section className={styles.container}>
      <div className={`${'container-common'} ${styles['container-review']}`}>
        <h1>Endless 5-Star Reviews</h1>
        <p className={styles.text}>A sample of the love</p>
        <div className={styles.cards}>
          <div onClick={prevSlide} className={`${styles.button} ${styles.prev}`}>
            <img src={images.prev} alt="" />
          </div>
          <div key={index} className={styles.card}>
            <p className={styles.message} >{reviewDisplay && reviewDisplay.content}</p>
            <div className={styles.info}>
              <img src={reviewDisplay.image ? reviewDisplay.image :
                (reviewDisplay.reviewer && reviewDisplay.reviewer.image ?
                  'http://localhost:8222/api/customers/images/'+reviewDisplay.reviewer.image
                  :images.defaultImage
                )
              } alt="" />
              <p >
                {reviewDisplay.reviewer && reviewDisplay.reviewer.user.fullName}
              </p>
            </div>
          </div>
          <div onClick={nextSlide} className={`${styles.button} ${styles.next}`}>
            <img src={images.next} alt="" />
          </div>
        </div>
        <div className={styles.logos}>
          <div className={styles['logos-display']}>
            <div>
              <img src={images.logo1} alt="" />
            </div>
            <div>
              <img src={images.logo2} alt="" />
            </div>
            <div>
              <img src={images.logo3} alt="" />
            </div>
            <div>
              <img src={images.logo4} alt="" />
            </div>
            <div>
              <img src={images.logo5} alt="" />
            </div>
            <div>
              <img src={images.logo6} alt="" />
            </div>
          </div>
          <div className={styles['logos-display']}>
            <div>
              <img src={images.logo7} alt="" />
            </div>
            <div>
              <img src={images.logo8} alt="" />
            </div>
            <div>
              <img src={images.logo9} alt="" />
            </div>
            <div>
              <img src={images.logo10} alt="" />
            </div>
            <div>
              <img src={images.logo11} alt="" />
            </div>
            <div>
              <img src={images.logo12} alt="" />
            </div>
            <div>
              <img src={images.logo13} alt="" />
            </div>
            <div>
              <img src={images.logo14} alt="" />
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
 
export default ReviewContent;