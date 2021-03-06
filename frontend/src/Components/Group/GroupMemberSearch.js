import React, { useState } from 'react';
import '../Other/SearchField.css';
import Snackbar from '../UI/Snackbar';
import store from '../../store';

import IconButton from '@material-ui/core/IconButton';
import Dialog from '@material-ui/core/Dialog';
import SearchIcon from '@material-ui/icons/Search';
import InputBase from '@material-ui/core/InputBase';
import AlertDialog from '../Other/AlertDialog';
import axios from 'axios';
import { Button, Avatar } from '@material-ui/core';

//type
// 0: 그룹 멤버 검색
// 1: 유저 검색후 선택
const GroupMemberSearch = (props) => {
  const [input, setInput] = useState('');
  const [alert, setAlert] = useState(null);
  const [list, setList] = useState(null);

  const onSearch = async (insert) => {
    if (insert === '') {
      setAlert(
        <AlertDialog
          severity='error'
          content='이름을 입력하세요'
          onAlertClose={() => {
            setAlert(null);
          }}
        />
      );
      return;
    }
    setAlert(<Snackbar onClose={() => setAlert(null)} severity='success' content='데이터 요청중...' />);
    try {
      const { data } = await axios.get('/followState/search', { params: { input: input } });

      console.log(data);
      if (data.length < 1) {
        setAlert(
          <AlertDialog
            severity='error'
            content='검색결과가 없습니다.'
            onAlertClose={() => {
              setAlert(null);
            }}
          />
        );
        return;
      }
      setAlert(null);
      setList(data);
    } catch (error) {
      console.log(error);
      setAlert(
        <AlertDialog
          severity='error'
          content='에러로인해 검색 실패하였습니다.'
          onAlertClose={() => {
            setAlert(null);
          }}
        />
      );
    }
  };

  return (
    <Dialog open style={{ backgroundColor: 'rgba(241, 242, 246,0.1)' }} onClose={() => props.onCancel()}>
      <div className='searchField-result'>
        <div className='searchField-title'>
          <InputBase
            placeholder='이름을 입력하세요'
            onChange={(e) => setInput(e.target.value)}
            style={{ color: '#ffffff' }}
            onKeyDown={(e) => {
              if (e.keyCode === 13) {
                onSearch(input);
              }
            }}
          />
          <IconButton type='submit' aria-label='search' onClick={() => onSearch(input)}>
            <SearchIcon style={{ color: '#ffffff' }} />
          </IconButton>
        </div>
        {list === null ? null : (
          <div style={{ maxHeight: '400px' }}>
            <ul>
              {(() => {
                return list.map((value) => {
                  return (
                    <li key={value.userInfo.userId}>
                      <Button
                        style={{
                          width: '100%',
                          justifyContent: 'flex-start',
                        }}
                        onClick={() => {
                          if (props.type === 1) {
                            props.onSelect(value.userInfo);
                            props.onCancel();
                            return;
                          }
                          try {
                            (async () => {
                              console.log({ groupCd: props.info.groupCd, userCd: props.info.userCd });
                              const { data } = await axios.post('/groupApply/create', {
                                groupCd: props.info.groupCd,
                                userCd: value.userInfo.userCd,
                                applyState: 1,
                              });
                              if (data) {
                                props.onAdd(value.userInfo);
                                store.dispatch({
                                  type: 'SAVE_NOTICE',
                                  notice: {
                                    noticeType: 'GROUP_APPLY_INVITE', // 이벤트 타입
                                    activeCd: props.info.groupCd, // 이벤트 주체
                                    targetCd: value.userInfo.userCd, // 이벤트 대상
                                  },
                                });
                              } else {
                                setAlert(
                                  <Snackbar
                                    onClose={() => setAlert(null)}
                                    severity='success'
                                    content='이미 초대를 보냈거나 신청을 받았습니다.'
                                  />
                                );
                              }
                            })();
                          } catch (error) {
                            setAlert(
                              <Snackbar
                                onClose={() => setAlert(null)}
                                severity='error'
                                content={`서버 에러 발생 ${error}`}
                              />
                            );
                          }
                        }}
                      >
                        <Avatar alt={`${value.userInfo.userId} img`} src={value.userInfo.userPic} />
                        &nbsp;&nbsp;&nbsp;
                        <span>{`${value.userInfo.userNm}(${value.userInfo.userId})`}</span>
                      </Button>
                    </li>
                  );
                });
              })()}
            </ul>
          </div>
        )}
      </div>
      {alert}
    </Dialog>
  );
};

export default GroupMemberSearch;
