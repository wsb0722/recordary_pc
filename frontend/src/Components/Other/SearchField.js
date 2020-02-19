import React, { useState } from 'react';
import InputBase from '@material-ui/core/InputBase';
import { fade, makeStyles } from '@material-ui/core/styles';
import SearchIcon from '@material-ui/icons/Search';
import SearchFieldResult from 'Components/Other/SearchFieldResult';
import axios from 'axios';


const useStyles = makeStyles(theme => ({
  search: {
    position: 'relative',
    borderRadius: theme.shape.borderRadius,
    backgroundColor: fade(theme.palette.common.white, 0.1),
    '&:hover': {
      backgroundColor: fade(theme.palette.common.white, 0.2),
    },
    marginLeft: 0,
    width: '100%',
    [theme.breakpoints.up('sm')]: {
      width: 'auto',
    },
    // border: '1px solid rgba(255,255,255,0.8)'
  },
  searchIcon: {
    width: theme.spacing(7),
    height: '100%',
    position: 'absolute',
    pointerEvents: 'none',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: fade(theme.palette.common.white, 0.8),
  },
  inputRoot: {
    color: 'inherit',
  },
  inputInput: {
    padding: theme.spacing(1, 1, 1, 7),
    transition: theme.transitions.create('width'),
    width: '100%',
    [theme.breakpoints.up('sm')]: {
      width: 120,
      '&:focus': {
        width: 200,
      },
    },
    color: 'white',
  },
}));

export default function SearchAppBar() {
  const classes = useStyles();
  const [searchState, setsearchState] = useState(false);
  const [userSearch, setUserSearch] = useState('');

  const setEnterKeyPress = () =>{
    if(searchState === true){
      return(
        <SearchFieldResult onCancel={() => setsearchState(false)}></SearchFieldResult>  
      )
    }
    return null;
  }

  const handleKeyPress = async (e) => {
    if(e.key === 'Enter') {
      
      // const Form = new FormData();
      // Form.append('userSearch', userSearch);

      // const { data } = await axios.post("http://localhost:8888/search", Form);
      // console.log(data);
      console.log(userSearch);
      setsearchState(true);
    }
  }

  return (
        <div>
          <div className={classes.search}>
            <div className={classes.searchIcon}>
              <SearchIcon />
            </div>
            <InputBase
                placeholder="Search…"
                classes={{
                    root: classes.inputRoot,
                    input: classes.inputInput,
              }}
              inputProps={{ 'aria-label': 'search' }}
              onKeyPress={handleKeyPress}
              onChange={(e)=>{
                setUserSearch(e.target.value);
              }}
            />
          </div>
          {setEnterKeyPress()}
      </div>
    )
  }
