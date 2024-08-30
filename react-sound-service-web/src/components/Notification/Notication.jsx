import { useContext, useEffect, useRef, useState } from "react";
import styles from "./Notification.module.css";
import { useNavigate } from "react-router-dom";
import { Context } from "../../common/ContextProvider";
import axios from "axios";
import SockJS from "sockjs-client/dist/sockjs";
import {over} from "stompjs";
import soundMessage from "../../assets/sound-message.mp3";

const Notification = ({hide}) => {

  const [notifications, setNotifications] = useState([]);

  const { checkTokenAndRefreshToken, role, checkRole, setNumberOfNotifications} = useContext(Context);

  const navigate = useNavigate();

  const clientRef = useRef(null);

  const [total, setTotal] = useState(0);

  const [isLoad, setIsLoad] = useState(false);


  useEffect(() => {
    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    checkRole();

    let id = null;
    if(localStorage.getItem('user')){
      id = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      id = JSON.parse(sessionStorage.getItem('user')).id;
    }

    if(role != null ){
      axios.get(`http://localhost:8222/api/notifications/find-by-artist-or-customer-id/${id}/${role}/0`, {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setNotifications(response.data.notifications);
        setTotal(response.data.total);
        setNumberOfNotifications(response.data.unReadTotal);
      })
      .catch(error => {
        console.log(error);
      })
    }    
  }, [role]);

  useEffect(() => {
    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    checkRole();

    let id = null;
    if(localStorage.getItem('user')){
      id = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      id = JSON.parse(sessionStorage.getItem('user')).id;
    }

    let idAndRole = id + ' ' + role;

    if(role) {
      const socket = new SockJS('http://localhost:8040/our-websocket?idAndRole='+idAndRole);
      const client = over(socket);
      clientRef.current = client;

      client.connect({}, () =>{
        client.subscribe('/user/topic/notifications', (message) => {
          const receivedMessage = JSON.parse(message.body);

          const sound = new Audio(soundMessage);
          sound.play();

          setNotifications(prev => {
            if (prev.length < 3) {
              // Thêm phần tử vào cuối mảng
              return [receivedMessage, ...prev];
            } else {
              // Nếu không, loại bỏ phần tử cuối cùng và thêm phần tử mới vào đầu mảng
              const removeLastElement = prev.slice(0, prev.length - 1);
              return [receivedMessage, ...removeLastElement];
            }
          });
          setTotal(prev => prev +1);
          setNumberOfNotifications(prev => prev +1);
        });
      });
    }

    return () => {
      if ((clientRef.current && clientRef.current.connected)) {
        clientRef.current.disconnect();
      }
      clientRef.current = null;
    };
  }, [role]);

  const readNotification = () => {
    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    const request = {
      ids: notifications.filter(notification => !notification.isRead)
                        .map(notification => notification.id)
    }

    if(request.ids.length >0) {
      axios.post('http://localhost:8222/api/notifications/read-notifications',request, {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      })
      .then(() => {
        setNotifications(prev => prev.map(notification => (
          request.ids.includes(notification.id)? {...notification, isRead: true}: notification
        )));

        setNumberOfNotifications(prev => prev - request.ids.length);
      })
      .catch(error => {
        console.log(error);
      });

    }
  }

  useEffect(() => {
    if(hide ==1) {
      document.getElementById('notification').style.display= 'none';
    }else {
      document.getElementById('notification').style.display= 'block';
      readNotification();
    }
  }, [hide]);

  useEffect(() => {
    if(hide ==0){
      readNotification();
    }
  }, [notifications]);

  const loadMore = () => {
    setIsLoad(true);
    
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

    const page = Math.floor(notifications.length / 3);
    if(role != null ){
      axios.get(`http://localhost:8222/api/notifications/find-by-artist-or-customer-id/${id}/${role}/${page}`, {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      })
      .then(response => {
        setNotifications(prev => [...prev, ...response.data.notifications]);
        setTotal(response.data.total);
        setNumberOfNotifications(response.data.unReadTotal);

        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      })
    }

  }

  return ( 
    <section id="notification" className={styles.notification}>
      {notifications && notifications.map(notification => (
        <div key={notification.id}>
          {notification.temporaryPurchasedBeatConfirmation && 
            <p>{`Vui lòng vào mail để thanh toán beat ${notification.temporaryPurchasedBeatConfirmation.beat.beatName} 
              của ${notification.temporaryPurchasedBeatConfirmation.seller.artistName}`}
            </p>
          }
          {notification.successfullPaymentNotification && 
            <p>
              {`${notification.successfullPaymentNotification.purchaser.user.fullName} vừa thanh toán 
                ${notification.successfullPaymentNotification.category}
                cho ${notification.successfullPaymentNotification.seller.artistName} thành công bằng 
                ${notification.successfullPaymentNotification.paymentMethod} ${notification.successfullPaymentNotification.amount}$`
              }
            </p>
          }
          {notification.hireConfirmation && (
            <>
              {notification.hireConfirmation.jobType ? (
                <p>
                  {`${notification.hireConfirmation.hirer.user.fullName} vừa thuê ${notification.hireConfirmation.expert.artistName}
                    với công việc ${notification.hireConfirmation.jobType.name} và giá là ${notification.hireConfirmation.price}$ 
                    bắt đầu từ ngày ${notification.hireConfirmation.startDate} đến ${notification.hireConfirmation.endDate}
                    hãy vào mail xác nhận nếu bạn là nghệ sĩ, vào mail để thanh toán nếu bạn là khách hàng`}
                </p>
              ) : (
                <p>
                  {`${notification.hireConfirmation.hirer.user.fullName} vừa thuê ${notification.hireConfirmation.expert.artistName}
                    với công việc kiểm tra âm thanh và giá là ${notification.hireConfirmation.price}$ 
                    bắt đầu từ ngày ${notification.hireConfirmation.startDate} đến ${notification.hireConfirmation.endDate}
                    hãy vào mail xác nhận nếu bạn là nghệ sĩ, vào mail để thanh toán nếu bạn là khách hàng`}
                </p>
              )}
            </>
          )}

          <p>{notification.notificationDate}</p>
          <p>{notification.notificationTime}</p>
        </div> 
      ))}
      {notifications && notifications.length < total &&
        <div onClick={loadMore} className={styles.more}>
          <button>Load more</button>
        </div>
      }

      {isLoad == true && 
        <div className="big-loader">
          <div className="loader"></div>
        </div>
      }
    </section>
  );
}
 
export default Notification;