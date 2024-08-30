import styles from "./Customer.module.css";
import images from "../../assets/images";
import { Link, useParams } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";

const Customer = () => {
  const [isLoad, setIsLoad] = useState(true);
  const {id} = useParams();
  const [customer, setCustomer] = useState({});

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
    <section className={styles.artist}>
      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </section>
  );
};

export default Customer;
