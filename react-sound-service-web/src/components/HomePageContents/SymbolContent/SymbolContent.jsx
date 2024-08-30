import styles from "./SymbolContent.module.css";
import images from "../../../assets/images";

const SymbolContent = () => {
  return (
    <section className={styles.container}>
      <div className={`${styles['container-flex']} ${'container-common'}`}>
        <div>
          <img src={images.symbol1} />
          <h4>The World's Best</h4>
          <p>Work with Grammy, Oscar, and Emmy winners from around the globe</p>
        </div>
        <div>
          <img src={images.symbol2} />
          <h4>Trusted Platform</h4>
          <p>Safe and secure with tens of thousands of verified reviews</p>
        </div>
        <div>
          <img src={images.symbol3} />
          <h4>Sound Your Best</h4>
          <p>Radio singles, YouTube hits, and chart-topping albums, all made on SoundBetter</p>
        </div>
      </div>
    </section>
  );
}
 
export default SymbolContent;