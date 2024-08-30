import axios from "axios";
import { createContext, useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import SockJS from "sockjs-client/dist/sockjs";
import {over} from "stompjs";
import soundMessage from "../assets/sound-message.mp3";

export const Context = createContext();

const ContextProvider = ({children}) => {
  const [displayVideo, setDisplayVideo] = useState(false);
  const [isForm, setIsForm] = useState(false);
  const [role, setRole] = useState();
  
  const [numberOfNotifications, setNumberOfNotifications] = useState(0);

  const clientRef = useRef(null);
  const [conversations, setConversations] = useState([]);
  const [totalConversations, setTotalConvetsations] = useState(0);
  const conversationsRef = useRef(conversations);
  const [latestConversations, setLatestConversations] = useState([]);
  const [total, setTotal] = useState(0);
  const latestConversationsRef = useRef(latestConversations);
  const [numberOfMessages, setNumberOfMessages] = useState(0);

  const navigate = useNavigate();

  const formatDate = (createdDate) => {
    const date = new Date(createdDate);
    const day = date.getDate().toString().padStart(2, '0'); 
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); 
    const year = date.getFullYear();

    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    const formattedDate = `${hours}:${minutes} ${day}/${month}/${year}`;
    return formattedDate;
  }

  const checkNumberOfMessageUnread = (accountOwnerIdRole) => {
    axios.get('http://localhost:8222/api/conversations/check-number-of-message-unread', {
      headers: {
        Authorization: 'Bearer '+ localStorage.getItem('access_token')
      },
      params: {
        accountOwnerIdRole: accountOwnerIdRole
      }
    })
    .then(response => {
      setNumberOfMessages(response.data);
    })
    .catch(error => {
      console.log(error);
    })
  }

  useEffect(() => {
    // 30s check token 1 lần
    // let intervalId; // Khai báo biến intervalId ở đây

    // if (localStorage.getItem('user') || sessionStorage.getItem('user')) {
    //   intervalId = setInterval(async () => {
    //     try {
    //       console.log('check token');
    //       await checkTokenAndRefreshToken();
    //     } catch (error) {
    //       console.error('Error checking token:', error);
    //     }
    //   }, 10000);
    // }

    // return () => {
    //   if (intervalId) {
    //     clearInterval(intervalId);
    //     console.log('Stopped checking token.');
    //   }
    // };


    axios.post
  }, []);

  useEffect(() => {
    checkRole();
    let id = null;
    if(localStorage.getItem('user')){
      id = JSON.parse(localStorage.getItem('user')).id;
    }

    if(sessionStorage.getItem('user')){
      id = JSON.parse(sessionStorage.getItem('user')).id;
    }

    if(role){
      if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
        navigate('/sign-in');
        return;
      }

      let idAndRole = id + ' ' + role;
      
      const checkTokenFunction = async () => {
        await checkTokenAndRefreshToken();
      }
  
      checkTokenFunction();

      checkNumberOfMessageUnread(idAndRole);
      

      const socket = new SockJS('http://localhost:8011/ws?idAndRole='+idAndRole);
      const client = over(socket);
      clientRef.current = client;

      client.connect({}, () =>{
        client.subscribe('/user/specific', async (message) => {
          const receivedMessage = JSON.parse(message.body);

          const checkTokenFunction = async () => {
            await checkTokenAndRefreshToken();
          }
      
          checkTokenFunction();

          if((receivedMessage.receiver.id + ' ' + receivedMessage.receiver.user.role) == idAndRole){
            const sound = new Audio(soundMessage);
            sound.play();
            checkNumberOfMessageUnread(idAndRole);
          }

          receivedMessage.createdDate = formatDate(receivedMessage.createdDate);

          let id = null;
          let role = null;
          if(localStorage.getItem('user')){
            id = JSON.parse(localStorage.getItem('user')).id;
            role = JSON.parse(localStorage.getItem('user')).user.role;
          }

          if(sessionStorage.getItem('user')){
            id = JSON.parse(sessionStorage.getItem('user')).id;
            role = JSON.parse(sessionStorage.getItem('user')).user.role;
          }
          let idRole = id + ' ' + role;
          
          if(conversationsRef.current.length >0) {
            let lastConversation = conversationsRef.current[conversationsRef.current.length-1];
            if(receivedMessage.receiver.id + ' ' + receivedMessage.receiver.user.role == idRole){
              if( ((lastConversation.sender.id + ' ' + lastConversation.sender.user.role) 
                == (receivedMessage.sender.id + ' ' + receivedMessage.sender.user.role))
                ||
                ((lastConversation.receiver.id + ' ' + lastConversation.receiver.user.role) 
                == (receivedMessage.sender.id + ' ' + receivedMessage.sender.user.role))
              ) {
                setConversations(prev => [...prev, receivedMessage]);
                setTotalConvetsations(prev => prev +1);
              }
            }else {
              setConversations(prev => [...prev, receivedMessage]);
              setTotalConvetsations(prev => prev +1);
            }
          }

          if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
            navigate('/sign-in');
            return;
          }
          
          checkTokenFunction();
          
          axios.get(`http://localhost:8222/api/conversations/find-latest-conversation`+
            `?accountOwnerIdRole=${idRole}&conversationId=${receivedMessage.id}`
          , {
              headers: {
                Authorization: 'Bearer ' + localStorage.getItem('access_token')
            },
          })
          .then(response => {
            const check = latestConversationsRef.current.some(item => item.id == response.data.id);
              if(check) {
              console.log(receivedMessage.createdDate)
              setLatestConversations((prev) => {
                const updatedConversations  = prev.map((conv) => {
                  if (conv.id === response.data.id) {
                    return {
                      ...conv,
                      conversation: receivedMessage,
                      dateTime: receivedMessage.createdDate
                    }
                  }
        
                    return conv;
                });
        
                const updatedConversation = updatedConversations.find(
                  (conv) => conv.conversation.id === receivedMessage.id
                );
            
                const remainingConversations = updatedConversations.filter(
                    (conv) => conv.conversation.id !== receivedMessage.id
                );
            
                return [updatedConversation, ...remainingConversations];
              });
            }else {
              setLatestConversations(prev => [response.data, ...prev]);
              response.data.dateTime = formatDate(response.data.dateTime);
              setTotal(prev => prev +1);
            }
          })
          .catch(error => {
            console.log(error);
          })
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

  const handleClickWatchVideo = () => {
    if(displayVideo == false ){
      setDisplayVideo(true);
    }else {
      setDisplayVideo(false);
    }
  };

  const turnOnOrOffForm = ()=> {
    if(isForm) {
      setIsForm(false);
    }else {
      setIsForm(true);
    }
  }

  const checkRole = () => {
    const userLocal = JSON.parse(localStorage.getItem('user'));
    const userSession = JSON.parse(sessionStorage.getItem('user'));
    if(userLocal) {
      setRole(userLocal.user.role);
    }

    if(userSession){
      setRole(userSession.user.role);
    }

    if(!userLocal && !userSession) {
      setRole(null);
    }
  }

  const generateNewToken = async (email, password) => {
    return await axios.post('http://localhost:8222/api/users/generate-new-token', {
      client_id: import.meta.env.VITE_CLIENT_ID,
      client_secret: import.meta.env.VITE_CLIENT_SECRET,
      grant_type: 'password',
      email: email,
      password: password
    } )
    .then(response => {
      return response.data;
    })
    .catch(error => {
      console.log(error);
    })
  }

  const checkToken = async (token) => {
    return await axios.post('http://localhost:8222/api/users/introspect-token', {
      client_id: import.meta.env.VITE_CLIENT_ID,
      client_secret: import.meta.env.VITE_CLIENT_SECRET,
      token: token,
    })
    .then( response => {
      return response.data.active;
    })
    .catch(error => {
      console.log(error);
    })
  }

  const logout = async (refresh_token) => {
    return await axios.post('http://localhost:8222/api/users/logout', {
      client_id: import.meta.env.VITE_CLIENT_ID,
      client_secret: import.meta.env.VITE_CLIENT_SECRET,
      refresh_token: refresh_token
    } )
    .catch(error => {
      console.log(error);
    })
  }

  const refreshToken = async (refresh_token) => {
    return await axios.post('http://localhost:8222/api/users/refresh-token', {
      client_id: import.meta.env.VITE_CLIENT_ID,
      client_secret: import.meta.env.VITE_CLIENT_SECRET,
      refresh_token: refresh_token,
      grant_type: 'refresh_token'
    } )
    .then(response => {
      return response.data;
    })
    .catch(error => {
      console.log(error);
    })
  }

  const checkTokenAndRefreshToken = async () => {
    if(! await checkToken(localStorage.getItem('access_token'))){
      if(! await checkToken(localStorage.getItem('refresh_token'))){
        localStorage.removeItem('user');
        sessionStorage.removeItem('user');
        navigate('/sign-in');
        return;
      }else {
        const tokenResponse = await refreshToken(localStorage.getItem('refresh_token'));
        localStorage.setItem('access_token', tokenResponse.access_token);
      }
    }
  }

  return ( 
    <Context.Provider value={{displayVideo, handleClickWatchVideo, isForm, turnOnOrOffForm, generateNewToken, checkToken, logout,
      refreshToken, role, checkRole, checkTokenAndRefreshToken, numberOfNotifications, setNumberOfNotifications,
      clientRef, conversations, setConversations, totalConversations, setTotalConvetsations, conversationsRef, latestConversations,
      setLatestConversations, total, setTotal, latestConversationsRef, formatDate, numberOfMessages, setNumberOfMessages
    }}>
      {children}
    </Context.Provider>
  );
}
 
export default ContextProvider;