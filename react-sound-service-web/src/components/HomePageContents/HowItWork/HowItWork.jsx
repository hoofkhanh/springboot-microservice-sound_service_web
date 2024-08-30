import styles from "./HowItWork.module.css";
import images from "../../../assets/images";

const HowItWork = () => {

  const handleOpacityCards = (event) => {
    document.getElementById('cards').style.opacity = '1';
    document.getElementById('box-content').style.opacity = '0';
    document.getElementById('sound-info').style.opacity = '0';

    event.target.style.color = 'white';
    event.target.querySelector('p').style.border = '4px solid white';
    event.target.querySelector('p').style.transform = 'scale(1.03)';

    let step1= document.getElementById('step-1');
    step1.style.removeProperty('color');
    step1.querySelector('p').style.removeProperty('border');
    step1.querySelector('p').style.removeProperty('transform');

    let step3= document.getElementById('step-3');
    step3.style.removeProperty('color');
    step3.querySelector('p').style.removeProperty('border');
    step3.querySelector('p').style.removeProperty('transform');

    let card2 = document.getElementById('card-2');
    card2.style.transform = 'rotate(-5deg)';
    card2.style.left = '-400px';
    card2.style.top = '-150px';
    card2.style.zIndex = '1';

    let card1 = document.getElementById('card-1');
    card1.style.transform = 'rotate(5deg)';
    card1.style.left = '0';
    card1.style.top = '-150px';
    card1.style.zIndex = '1';
  }

  const handleOpacityBoxContent = (event) => {
    document.getElementById('box-content').style.opacity = '1';
    document.getElementById('cards').style.opacity = '0';
    document.getElementById('sound-info').style.opacity = '0';

    event.target.style.color = 'white';
    event.target.querySelector('p').style.border = '4px solid white';
    event.target.querySelector('p').style.transform = 'scale(1.03)';

    let step2= document.getElementById('step-2');
    step2.style.removeProperty('color');
    step2.querySelector('p').style.removeProperty('border');
    step2.querySelector('p').style.removeProperty('transform');

    let step3= document.getElementById('step-3');
    step3.style.removeProperty('color');
    step3.querySelector('p').style.removeProperty('border');
    step3.querySelector('p').style.removeProperty('transform');

    let card2 = document.getElementById('card-2');
    card2.style.removeProperty('transform');
    card2.style.removeProperty('left');
    card2.style.removeProperty('top');
    card2.style.removeProperty('zIndex');

    let card1 = document.getElementById('card-1');
    card1.style.removeProperty('transform');
    card1.style.removeProperty('left');
    card1.style.removeProperty('top');
    card1.style.removeProperty('zIndex');
  }

  const handleOpacitySoundInfo= (event) => {
    document.getElementById('sound-info').style.opacity = '1';
    document.getElementById('box-content').style.opacity = '0';
    document.getElementById('cards').style.opacity = '0';

    event.target.style.color = 'white';
    event.target.querySelector('p').style.border = '4px solid white';
    event.target.querySelector('p').style.transform = 'scale(1.03)';

    let step1= document.getElementById('step-1');
    step1.style.removeProperty('color');
    step1.querySelector('p').style.removeProperty('border');
    step1.querySelector('p').style.removeProperty('transform');

    let step2= document.getElementById('step-2');
    step2.style.removeProperty('color');
    step2.querySelector('p').style.removeProperty('border');
    step2.querySelector('p').style.removeProperty('transform');

    let card2 = document.getElementById('card-2');
    card2.style.removeProperty('transform');
    card2.style.removeProperty('left');
    card2.style.removeProperty('top');
    card2.style.removeProperty('zIndex');

    let card1 = document.getElementById('card-1');
    card1.style.removeProperty('transform');
    card1.style.removeProperty('left');
    card1.style.removeProperty('top');
    card1.style.removeProperty('zIndex');
  }

  return ( 
    <section className={styles.container}>
      <div className={`${styles['how-it-work']} ${'container-common'}`}>
        <div  className={styles.step}>
          <h1>How SoundBetter works:</h1>
          <div id="step-1" onMouseEnter={(event) => handleOpacityBoxContent(event)} className={styles['step-1']}>
            <p>1</p>
            <h3 >Describe your project in seconds</h3>
          </div>
          <div id="step-2" onMouseEnter={(event) => handleOpacityCards(event)} className={styles['step-2']}>
            <p>2</p>
            <h3>Get free proposals from top professionals</h3>
          </div>
          <div id="step-3" onMouseEnter={(event) => handleOpacitySoundInfo(event)} className={styles['step-3']}>
            <p>3</p>
            <h3>Hire a pro and get awesome sounding tracks</h3>
          </div>
        </div>
        <div className={styles.show}>
          <div id="box-content" className={styles['box-content-parent']}>
            <div className={styles['box-content']}>
              <p>Tell us about your project:</p>
              <textarea value="I've recorded 3 songs and am looking for a professional 
                mixing engineer to get it ready for release. Each song has around 
                40 recorded tracks. It is mostly live instrumentation with a few added electronic"
                  disabled>
              </textarea>
            </div>
          </div>
          <div id="cards" className={styles.cards}>
              <div id="card-1" className={`${styles.card} ${styles['card-1']}`}>
                <div className={styles.info}>
                  <img className={styles.avatar} src={images.hiw1} />
                  <h4>MARTIN MERENYI</h4>
                  <p className={styles.skill}>Mixing & Mastering Engineer</p>
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <p className={styles.price}>$300</p>
                </div>
                <div className={styles.message}>
                  Hey Aron, Thanks a lot for your enq...
                </div>
              </div>
              <div id="card-2" className={`${styles.card} ${styles['card-2']}`}>
                <div className={styles.info}>
                  <img className={styles.avatar} src={images.hiw2} />
                  <h4>GEKKO</h4>
                  <p className={styles.skill}>Singer</p>
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <p className={styles.price}>$400</p>
                </div>
                <div className={styles.message}>
                  Hey Aron, i got your question...
                </div>
              </div>
              <div className={`${styles.card} ${styles['card-3']}`}>
                <div className={styles.info}>
                  <img className={styles.avatar} src={images.hiw3} />
                  <h4>TRAVIS SCOTT</h4>
                  <p className={styles.skill}>Producer</p>
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <img className={styles.stars} src={images.star} alt="" />
                  <p className={styles.price}>$500</p>
                </div>
                <div className={styles.message}>
                  Hey Aron, i got it...
                </div>
              </div>
          </div>
          <div id="sound-info" className={styles['sound-info']}>
            <div className={styles['sound-card']}>
              <div>
                <p className={styles.title}>PROJECT STATUS</p>
                <h4 >Ready For Review</h4>
                <img className={styles.soundWave} src={images.soundWave} alt="" />
              </div>
              <div className={styles.info}>
                <img src={images.hiw3} alt="" />
                <div>
                  <h4>TRAVIS SCOTT</h4>
                  <p className={styles.skill}>Producer</p>
                  <p>Hi my name Travis ...</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
 
export default HowItWork;