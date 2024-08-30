import styles from "./Message.module.css";
import images from "../../assets/images";
import { useContext, useEffect, useRef, useState } from "react";
import stay from "../../assets/STAY.mp3";
import { Context } from "../../common/ContextProvider";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import SockJS from "sockjs-client/dist/sockjs";
import {over} from "stompjs";

const Message = () => {
  const [displayImage, setDisplayImage ] = useState(false);


  const [displayAvatar, setDisplayAvatar] = useState(null);
  const [roleDisplay, setRoleDisplay] = useState(null);
  const [nameDisplay, setNameDisplay] =  useState(null);
  const [idRole, setIdRole] = useState(null);

  const [message, setMessage] = useState('');

  const { checkTokenAndRefreshToken, role, checkRole, clientRef, conversations, setConversations, 
    totalConversations, setTotalConvetsations, conversationsRef, latestConversations,
    setLatestConversations, total, setTotal, latestConversationsRef, formatDate, setNumberOfMessages
      } = useContext(Context);

  const scroolbarConversation = useRef(null);
  const [prevTotalScrobarHeight, setPrevTotalScrobarHeight] = useState(0);

  const [nameSearch, setNameSearch] = useState('');
  const [timer, setTimer] = useState(null);
  const[isSearch, setIsSearch ] = useState(false);

  const navigate = useNavigate();

  const [lastAccess, setLastAccess] = useState(null);

  const fetchLatestConversations = (id, role, page) => {
    setIsSearch(false);

    axios.get(`http://localhost:8222/api/conversations/find-latest-conversation-by-account-owner-id-role/${id} ${role}`, {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('access_token')
      },
      params: {
        page: page
      }
    })
    .then(response => {
      const updatedConversations = response.data.latestConversations.map(conversation => {
        return {
          ...conversation, 
          dateTime: formatDate(conversation.dateTime)
        };
      });
      if(page ==0 ) {
        setLatestConversations(updatedConversations);
      }else {
        setLatestConversations(prev => {
            const seen = new Set();
            // Lọc các phần tử duy nhất dựa trên ID cuộc trò chuyện
            const uniqueConversations = [...prev, ...updatedConversations].filter((conv) => {
                if (seen.has(conv.id)) {
                    return false; // Nếu ID đã thấy, bỏ qua
                }
                seen.add(conv.id); // Thêm ID vào Set
                return true;
            });
  
            // Trả về danh sách các cuộc trò chuyện duy nhất
            return uniqueConversations;
          });
      }

      setTotal(response.data.total);
    }).catch(error => {
      console.log(error);
    });
  }

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

    if(role) {
      let id = null;
      if(localStorage.getItem('user')){
        id = JSON.parse(localStorage.getItem('user')).id;
      }

      if(sessionStorage.getItem('user')){
        id = JSON.parse(sessionStorage.getItem('user')).id;
      }

      setIdRole(id + ' ' + role);

      fetchLatestConversations(id, role, 0);
    }
  }, [role]);

  useEffect(() => {
    conversationsRef.current = conversations;
    if(prevTotalScrobarHeight == 0) {
      scroolbarConversation.current.scrollTop = scroolbarConversation.current.scrollHeight ;
    }else {
      scroolbarConversation.current.scrollTop = scroolbarConversation.current.scrollHeight  - prevTotalScrobarHeight;
    }
  }, [conversations]);

  useEffect(() => {
    latestConversationsRef.current = latestConversations;
  }, [latestConversations]);

  const fetchConversation = (conversation, page) => {
    axios.get(`http://localhost:8222/api/conversations/find-conversation-of-two-people/` +
      `${conversation.sender.id} ${conversation.sender.user.role}` + 
      `/${conversation.receiver.id} ${conversation.receiver.user.role}`, {
      headers: {
        Authorization: 'Bearer ' +localStorage.getItem('access_token')
      }, 
      params: {
        page: page
      }
    })
    .then(response => {
      const updatedConversations = response.data.conversations.map(conversation => {
        return {
          ...conversation, 
          createdDate: formatDate(conversation.createdDate)
        };
      });
      if(page ==0){
        setPrevTotalScrobarHeight(0);

        setConversations(updatedConversations);
      }else {
        setPrevTotalScrobarHeight(scroolbarConversation.current.scrollHeight);
        
        setConversations(prev => {
          const seen = new Set();
          // Lọc các phần tử duy nhất dựa trên ID cuộc trò chuyện
          const uniqueConversations = [...updatedConversations, ...prev].filter((conv) => {
              if (seen.has(conv.id)) {
                  return false; // Nếu ID đã thấy, bỏ qua
              }
              seen.add(conv.id); // Thêm ID vào Set
              return true;
          });

          // Trả về danh sách các cuộc trò chuyện duy nhất
          return uniqueConversations;
        });
        
      }
      
      setTotalConvetsations(response.data.total);
    })
    .catch(error => {
      console.log(error);
    });
  }

  const getLastAccessOfUser = async (email) => {
    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();
    
    let token = null;
    await axios.post('http://localhost:8222/api/users/get-token-no-need-login', {
      client_id : import.meta.env.VITE_CLIENT_ID,
      client_secret : import.meta.env.VITE_CLIENT_SECRET,
      grant_type: 'client_credentials'
    })
    .then(response => {
      token = response.data.access_token;
    })
    .catch(error => {
      console.log(error);
    })

    axios.get('http://localhost:8222/api/users/get-last-access-of-user/keycloak', {
      params: {
        token: token,
        client_id_real: import.meta.env.VITE_CLIENT_ID_REAL
      },
      headers: {
        Authorization: 'Bearer ' +localStorage.getItem('access_token')
      }
    })
    .then(response => {
      const data = response.data.filter(data => data.username == email);
      if(data.length >0){
        setLastAccess('Truy cập vào ' + formatDate(new Date(data[0].lastAccess)))
      }else {
        setLastAccess('Off');
      }
      
    })
    .catch(error => {
      console.log(error);
    })
  } 

  const clickToConversation = async (latestConversationId ,conversation, accountOwner) => {
    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    checkRole();

    let senderIdRole = null;
    if((conversation.sender.id + ' ' + conversation.sender.user.role) != (accountOwner.id + ' ' + accountOwner.user.role)){
      senderIdRole = conversation.sender.id + ' ' + conversation.sender.user.role;
      setDisplayAvatar(conversation.sender.image);
      setRoleDisplay(conversation.sender.user.role);
      setNameDisplay(conversation.sender.artistName? conversation.sender.artistName: conversation.sender.user.fullName);
      getLastAccessOfUser(conversation.sender.user.email);
    }else {
      setDisplayAvatar(conversation.receiver.image);
      setRoleDisplay(conversation.receiver.user.role);
      setNameDisplay(conversation.receiver.artistName? conversation.receiver.artistName: conversation.receiver.user.fullName);
      getLastAccessOfUser(conversation.receiver.user.email);
    }
    
    

    const request = {
      senderIdRole : senderIdRole,
      receiverIdRole: accountOwner.id + ' ' + accountOwner.user.role
    }


    if(!conversation.isRead && 
      ((conversation.receiver.id + ' ' + conversation.receiver.user.role) ==  (accountOwner.id + ' ' + accountOwner.user.role))){
      axios.post('http://localhost:8222/api/conversations/read-message', request, {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      })
      .then(() => {
        setNumberOfMessages(prev => prev -1);
        setLatestConversations(prev => 
          prev.map(latestConversation => 
            latestConversation.id === latestConversationId 
            ? {...latestConversation, conversation: {
                  ...latestConversation.conversation,
                  isRead: true
                }
              }
            : latestConversation 
          )
        )
      })
      .catch(error => {
        console.log(error);
      })
    }
    
    fetchConversation(conversation, 0);

    if(isSearch == true) {
      let id = null;
      if(localStorage.getItem('user')){
        id = JSON.parse(localStorage.getItem('user')).id;
      }

      if(sessionStorage.getItem('user')){
        id = JSON.parse(sessionStorage.getItem('user')).id;
      }

      setNameSearch('');
      fetchLatestConversations(id, role, 0);
    }
  }

  const handleSendMessage = (conversation, message, e) => {
    e.preventDefault();

    if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
      navigate('/sign-in');
      return;
    }

    const checkTokenFunction = async () => {
      await checkTokenAndRefreshToken();
    }

    checkTokenFunction();

    let receiverIdRole = null;
    if(idRole == conversation.sender.id + ' ' + conversation.sender.user.role){
      receiverIdRole = conversation.receiver.id + ' ' + conversation.receiver.user.role;
    }else{
      receiverIdRole= conversation.sender.id + ' ' + conversation.sender.user.role;
    }

    const request = {
      oldId: conversation.id,
      senderIdRole: idRole,
      receiverIdRole: receiverIdRole,
      message: message
    }

    clientRef.current.send('/app/private', {}, JSON.stringify(request));

    setMessage('');
    setPrevTotalScrobarHeight(0);
  }

  const handleLoadMoreLatestConversation = () => {
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

    fetchLatestConversations(id, role, Math.floor(latestConversations.length /4));
  }

  const handleLoadMoreConversation = (e) => {
    const { scrollTop } = e.target;

    // Kiểm tra xem đã cuộn đến cuối chưa
    if (scrollTop == 0 && (conversations.length < totalConversations)) {
      fetchConversation(conversations[conversations.length-1], Math.floor(conversations.length /10) );
    }
  }

  useEffect(() => {
    
    if(nameSearch != '') {
      setIsSearch(true);
    }else {
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

      fetchLatestConversations(id, role ,0);
    }

    if(timer){
      clearTimeout(timer);
    }

    const newTimer = setTimeout(() => {
      if(!localStorage.getItem('user') && !sessionStorage.getItem('user')){
        navigate('/sign-in');
        return;
      }
  
      const checkTokenFunction = async () => {
        await checkTokenAndRefreshToken();
      }
  
      checkTokenFunction();

      if(nameSearch != ''){
        axios.get('http://localhost:8222/api/conversations/find-latest-conversation-by-name', {
          params: {
            accountOwnerIdRole: idRole,
            name: nameSearch
          },
          headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token')
          }
        })
        .then(response => {
          const updatedConversations = response.data.map(latestConversation => {
            return {
              ...latestConversation, 
              dateTime: formatDate(latestConversation.dateTime)
            };
          });
          setLatestConversations(updatedConversations);
        })
        .catch(error => {
          console.log(error);
        })
      }
    }, 1000);

    setTimer(newTimer);
  },[nameSearch]);

  return ( 
    <section className={styles.message}>
      <div className="container-common">
        <h1>Message</h1>
        <div className={styles['big-input']}>
          <div className={styles.input}>
            <input value={nameSearch} 
              onChange={(e) => setNameSearch(e.target.value)} 
              type="text" placeholder='Enter name...' />
            <img src={images.search} />
          </div>
        </div>
      </div>
      <div className={styles.main}>
        <div>
          <div className={styles.type}>
            <span>All</span>
            <span>Unread</span>
          </div>
          <div className={styles.inboxs}>
            {latestConversations && latestConversations.map(latestConversation => (
              <div onClick={() => clickToConversation(latestConversation.id , latestConversation.conversation, latestConversation.accountOwner)} 
                key={latestConversation.id} className={styles.inbox}> 
                <div className={styles.avatar}>
                  {
                    latestConversation.accountOwner.id + ' ' + latestConversation.accountOwner.user.role
                    != latestConversation.conversation.sender.id + ' ' + latestConversation.conversation.sender.user.role &&
                    <img src={latestConversation.conversation.sender.user.role == 'ARTIST' ? 
                      `http://localhost:8222/api/artists/images/${latestConversation.conversation.sender.image}`
                      :`http://localhost:8222/api/customers/images/${latestConversation.conversation.sender.image}`}  />
                  }
                  {
                    latestConversation.accountOwner.id + ' ' + latestConversation.accountOwner.user.role
                    != latestConversation.conversation.receiver.id + ' ' + latestConversation.conversation.receiver.user.role &&
                    <img src={latestConversation.conversation.receiver.user.role == 'ARTIST' ? 
                      `http://localhost:8222/api/artists/images/${latestConversation.conversation.receiver.image}`
                      :`http://localhost:8222/api/customers/images/${latestConversation.conversation.receiver.image}`}  />
                  }
                  
                </div>
                <div className={styles.info}>
                  <div className={styles['main-info']}>
                    {
                      latestConversation.accountOwner.id + ' ' + latestConversation.accountOwner.user.role
                      != latestConversation.conversation.sender.id + ' ' + latestConversation.conversation.sender.user.role &&
                      <h4>
                        {
                          latestConversation.conversation.sender.artistName ? latestConversation.conversation.sender.artistName
                          : latestConversation.conversation.sender.user.fullName
                        }
                      </h4>
                    }
                    {
                      latestConversation.accountOwner.id + ' ' + latestConversation.accountOwner.user.role
                      != latestConversation.conversation.receiver.id + ' ' + latestConversation.conversation.receiver.user.role &&
                      <h4>
                        {
                          latestConversation.conversation.receiver.artistName ? latestConversation.conversation.receiver.artistName
                          : latestConversation.conversation.receiver.user.fullName
                        }
                      </h4>
                    }
                    {
                      latestConversation.accountOwner.id + ' ' + latestConversation.accountOwner.user.role
                      == latestConversation.conversation.sender.id + ' ' + latestConversation.conversation.sender.user.role &&
                      <p>Bạn: {latestConversation.conversation.message}</p>
                    }
                    {
                      latestConversation.accountOwner.id + ' ' + latestConversation.accountOwner.user.role
                      == latestConversation.conversation.receiver.id + ' ' + latestConversation.conversation.receiver.user.role &&
                      <p className={!latestConversation.conversation.isRead  ? styles.unread : ''}>{latestConversation.conversation.message}</p>
                    }
                  </div>
                  <div className={styles.remove}>
                    <span>{latestConversation.dateTime}</span>
                  </div>
                </div>
              </div>
            ))}
            {latestConversations && latestConversations.length < total && !isSearch &&
              <div className={styles.button}>
                <button onClick={handleLoadMoreLatestConversation}>Load More</button>
              </div>
            }
          </div>
        </div>
        <div className={styles.content}>
          <div className={styles['info-bar']}>
            <div className={styles.info}>
              <div className={styles.avatar}>
                {displayAvatar && roleDisplay && roleDisplay == 'CUSTOMER' &&
                  <img src={`http://localhost:8222/api/customers/images/${displayAvatar}`} />
                }
                {displayAvatar && roleDisplay && roleDisplay == 'ARTIST' &&
                  <img src={`http://localhost:8222/api/artists/images/${displayAvatar}`} />
                }
              </div>
              <div className={styles['main-info']}>
                <h4>{nameDisplay}</h4>
                <p>{nameDisplay && lastAccess}</p>
              </div>
            </div>
          </div>
          <div className={styles['main-message']}>
            <div className={styles['tool']}>
              <div ref={scroolbarConversation} onScroll={(e) => handleLoadMoreConversation(e)} className={styles['display-message']}>
                {conversations && conversations.map((conversation, index) => (
                  (conversation.sender.id + ' ' + conversation.sender.user.role) != idRole 
                  ? (
                    <div key={conversation.id} className={styles.conversationOfOtherPerson}>
                      <div className={styles.avatar}>
                        {displayAvatar && roleDisplay && roleDisplay == 'CUSTOMER' &&
                          <img src={`http://localhost:8222/api/customers/images/${displayAvatar}`} />
                        }
                        {displayAvatar && roleDisplay && roleDisplay == 'ARTIST' &&
                          <img src={`http://localhost:8222/api/artists/images/${displayAvatar}`} />
                        }
                      </div>
                      <div className={styles.text}>
                        <div className={styles['single-text']}>
                          {conversation.message}
                          <div className={styles['hidden-time']}>
                            {conversation.createdDate}
                          </div>
                        </div>
                      </div>
                    </div>
                  ) : (
                    <div key={conversation.id} className={styles.conversationOfMe}>
                      <div className={styles.text}>
                        <div className={styles['single-text']}>
                          {conversation.message}
                          <div className={styles['hidden-time']}>
                            {conversation.createdDate}
                          </div>
                        </div>
                      </div>
                    </div>
                  )
                  
                ))}

                {/* <div className={styles.conversationOfOtherPerson}>
                  <div className={styles.avatar}>
                    <img src={images.avatar1} />
                  </div>
                  <div className={styles.text}>
                    <div  className={styles['single-image']}>
                      <img onClick={() => setDisplayImage(prev => !prev)} src={images.nhi} alt="" />
                      <div className={styles['hidden-time']}>
                        12:30 18/08/2003
                      </div>
                    </div>
                    <div  className={styles['single-image']}>
                      <img onClick={() => setDisplayImage(prev => !prev)} src={images.nhi} alt="" />
                      <div className={styles['hidden-time']}>
                        12:30 18/08/2003
                      </div>
                      <div className={styles.time}>
                        15:30
                      </div>
                    </div>
                  </div>
                </div> */}
                
                
              </div>
              {conversations.length >0 && 
                <form onSubmit={(e) => handleSendMessage(conversations[conversations.length-1], message, e)}>
                  <div className={styles.enterInput}>
                    <input type="text" placeholder="....." value={message} onChange={(e) => setMessage(e.target.value)} />
                    {message != '' &&
                      <button >Send</button>
                    }
                    <div>
                      <input id="file" type="file" multiple />
                      <label htmlFor="file">
                        Attach Files
                      </label>
                    </div>
                  </div>
                </form>
              }
            </div>
          </div>
        </div>
      </div>

      {displayImage && 
        <div className="loadImageBig">
          <div className='loadImage'>
            <div className="remove" onClick={() => setDisplayImage(prev => !prev)}>
              X
            </div>
            <img src={images.nhi} alt="" />
          </div>
        </div>
      }
    </section>
  );
}
 
export default Message;