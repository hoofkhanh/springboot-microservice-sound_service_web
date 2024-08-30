import styles from "./Customer.module.css";
import images from "../../../assets/images";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";

const Customer = ({id}) => {

  const [customer, setCustomer] = useState({});
  const [isLoad, setIsLoad] = useState(true);

  useEffect( () => {
    setIsLoad(true);

     axios.get(`http://localhost:8222/api/customers/${id}`)
    .then(response => {
      setCustomer(response.data);
      setIsLoad(false);
    })
    .catch(error => {
      console.log(error);
    });
  },[]);

  return ( 
    <div className={styles.container}>
      <div className={styles['hero-info']}>
        <h1>{customer.user && customer.user.fullName}</h1>
        <h3>CUSTOMER</h3>
        <div className={styles.country}>
          <Link to='#'>{customer.user && customer.user.country}</Link>
        </div>
      </div>
      <div className={styles['hero-image']}>
        <img id="hero-image" src={customer.image ? 
            'http://localhost:8222/api/customers/images/'+customer.image: 
            images.defaultImage} alt=""  />
      </div>
      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </div>
   );
}
 
export default Customer;