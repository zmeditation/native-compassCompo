import React from 'react';
import { requireNativeComponent } from 'react-native';

type Props = {
  bearing: Number,
  width: Number,
  height: Number,
  accuracyCallBack: Function
}

const RCTCustomView = requireNativeComponent('RCTMyCustomView', CompassComponent, {});

class CompassComponent extends React.PureComponent<Props> {
    _accuracyCallBack = (accuracy) => {
      if(!this.props.accuracyCallBack){
        return;
      }
      this.props.accuracyCallBack(accuracy);
    }


  render() {
    return <RCTCustomView {...this.props} accuracyCallBack={this._accuracyCallBack}/>
  }
} 

export default CompassComponent;