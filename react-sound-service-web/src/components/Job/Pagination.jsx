import { Link } from "react-router-dom";
import styles from "./Pagination.module.css";
import { useEffect, useState } from "react";
import axios from "axios";

const Pagination = ({ totalJobProp, isFilter, filteredJobType, setIsLoad, setPostedJobs, setTotal, indexPass }) => {
  const [totalPage, setTotalPage] = useState([]);

  const [activedPage, setActivedPage] = useState(0);

  const [maxPage, setMaxPage] = useState(0);
  const [minPage, setMinPage] = useState(0);

  useEffect(() => {
    setActivedPage(1);
    
    const totalPageTemp = Math.ceil(totalJobProp / 3);
    const totalPageArrayTemp = [];

    if(totalPageTemp >=2 ){
      setMaxPage(2);
    }else {
      setMaxPage(1);
    }
    setMinPage(1);

    for (let i = 1; i <= totalPageTemp; i++) {
      totalPageArrayTemp.push(i);
    }

    setTotalPage(totalPageArrayTemp);
  }, [totalJobProp, indexPass]);

  const handlePageChange = async (e) => {
    setIsLoad(true);

    let prevPage = null;
    Array.from(document.getElementsByClassName('active-pagination')).forEach(element => {
      if(element.classList.contains('active-pagination') ){
        prevPage = element.textContent;
        element.classList.remove('active-pagination');
      }
    })

    e.target.classList.add('active-pagination');
    const page = e.target.textContent - 1;
    setActivedPage(e.target.textContent);
    if(isFilter == true) {
      const genreJob = filteredJobType;
      
      await axios.get('http://localhost:8222/api/jobs/posted-job/find-by-job-type-id/'+genreJob, {
        params: {
          page: page
        }
      })
      .then(async response => {
        setTotal(response.data.total);
        setPostedJobs(response.data.postedJobs);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      });
    }else {
      await axios.get('http://localhost:8222/api/jobs/posted-job', {
        params: {
          page: page
        }
      })
      .then(async response => {
        setTotal(response.data.total);
        setPostedJobs(response.data.postedJobs);
        setIsLoad(false);
      })
      .catch(error => {
        console.log(error);
      });
    }
  }

  const nextPage =() => {
    const realMaxPage = totalPage[totalPage.length -1];

    if(maxPage +2 > realMaxPage) {
      setMaxPage(realMaxPage);
      setMinPage(realMaxPage);
    }else {
      setMaxPage(prev => prev +2);
      setMinPage(prev => prev +2);
    }
  }

  const previousPage =() => {
    if(minPage -2 <=0) {
      setMinPage(1);
      setMaxPage(2);
    } else {
      if(maxPage == minPage) {
        setMaxPage(prev => prev -1);
      }else {
        setMaxPage(prev => prev -2);
      }
      
      setMinPage(prev => prev -2);
    }
    
  }

  return (
    <div className={styles.pagination}>
      <div>
        <ul>
          {minPage > 1 && 
            <li><Link onClick={previousPage} to='#'>&lt;</Link></li>
          }
            {totalPage.map((number) => {
              return  number <= maxPage && number >= minPage ? (
                <li key={number}>
                  <Link className={activedPage == number? 'active-pagination': null} onClick={handlePageChange} to="#">{number}</Link>
                </li>
              ) : null;
            })}
          {maxPage < totalPage.length && 
            <li><Link onClick={nextPage} to='#'>&gt;</Link></li>
          }
        </ul>
      </div>
    </div>
  );
};

export default Pagination;
