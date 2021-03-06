import React from 'react';
import Dialog from '@material-ui/core/Dialog';
import Alert from '@material-ui/lab/Alert';
import AlertTitle from '@material-ui/lab/AlertTitle';
import Button from '@material-ui/core/Button';
import axios from 'axios';

// severity (success or error)
// <AlertDialog severity='error' content='' onAlertClose={} onAlertSubmit={}/>

class AlertDialog extends React.Component {
  render() {
    return (
      <Dialog open>
        <div>
          <Alert severity={this.props.severity}>
            <AlertTitle>{this.props.severity}</AlertTitle>
            {this.props.content}
            {this.props.onAlertSubmit === undefined ? null : (
              <Button style={{ marginTop: '10px' }} onClick={() => this.props.onAlertSubmit()}>
                확인
              </Button>
            )}
            <Button
              style={{ marginTop: '10px' }}
              onClick={() => {
                this.props.onAlertClose();
              }}
            >
              닫기
            </Button>
          </Alert>
        </div>
      </Dialog>
    );
  }
}

export default AlertDialog;
