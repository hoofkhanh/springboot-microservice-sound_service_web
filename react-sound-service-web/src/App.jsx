import './App.css';
import Header from './components/Headers/Header/Header';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePageContent from './components/HomePageContents/HomePageContent/HomePageContent';
import Footer from './components/Footer/Footer';
import ExploreContent from './components/ExploreContent/Explore/ExploreContent';
import Artist from './components/ExploreContent/Artist/Artist';
import Job from './components/Job/Job';
import JobDetail from './components/Job/JobDetail/JobDetail';
import Beat from './components/Beat/Beat';
import SoundCheck from './components/SoundCheck/SoundCheck';
import SoundChecker from './components/SoundChecker/SoundChecker';
import Customer from './components/Customer/Customer';
import SignIn from './components/SignIn/SignIn';
import MyJob from './components/Job/MyJob/MyJob';
import ContextProvider from './common/ContextProvider';
import Favorite from './components/Favorite/Favorite';
import MyPostedBeat from './components/Beat/MyBeat/MyPostedBeat';
import MyPurchasedBeat from './components/Beat/MyPurchasedBeat/MyPurchasedBeat';
import MyTask from './components/MyTask/MyTask';
import RentedCustomerTask from './components/RentedCustomerTask/RentedCustomerTask';
import Message from './components/Message/Message';
import Signup from './components/Signup/Signup';
import MyProfile from './components/MyProfile/MyProfile';

function App() {

  return (
    <>
      <Router>
        <ContextProvider>
          <Header/>
          <Routes>
            <Route path='/' element={<HomePageContent/>}/>
            <Route path='/explore' element={<ExploreContent/>}/>
            <Route path='/artists/:id' element={<Artist/>}/>
            <Route path='/customers/:id' element={<Customer/>}/>
            <Route path='/jobs' element={<Job/>}/>
            <Route path='/job-details/:id' element={<JobDetail/>}/>
            <Route path='/beats' element={<Beat/>}/>
            <Route path='/sound-checks' element={<SoundCheck/>}/>
            <Route path='/sound-checkers/:id' element={<SoundChecker/>}/>
            <Route path='/sign-in' element={<SignIn/>}/>
            <Route path='/my-jobs' element={<MyJob/>}/>
            <Route path='/favorites' element={<Favorite/>}/>
            <Route path='/my-posted-beats' element={<MyPostedBeat/>}/>
            <Route path='/my-purchased-beats' element={<MyPurchasedBeat/>}/>
            <Route path='/my-tasks' element={<MyTask/>}/>
            <Route path='/rented-customer-tasks' element={<RentedCustomerTask/>}/>
            <Route path='/messages' element={<Message/>}/>
            <Route path='/sign-up' element={<Signup/>}/>
            <Route path='/profile' element={<MyProfile/>}/>
          </Routes>
          <Footer/>
        </ContextProvider>
      </Router>
    </>
  )
}

export default App
