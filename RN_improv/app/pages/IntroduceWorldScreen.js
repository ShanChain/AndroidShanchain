/**
 * Created by wangweilin on 2017-11-28.
 * @providesModule IntroduceWorldScreen
 */


'use strict';

import React, {Component} from 'react';
import {
    StyleSheet,
    View,
    Dimensions,
    TextInput,
} from 'react-native';

import {
    StackNavigator,
} from 'react-navigation';


const {width, height, scale, fontScale} = Dimensions.get('window');
const screenScale = width / 375;

class IntroduceWorldScreen extends Component {
    constructor(props) {
        super(props);

    }

    componentWillMount() {

    }

    refreshData() {

    }

    render() {
        return (
            <View style={styles.container}>
                <TextInput placeholder='简单介绍一下世界吧~'
                           multiline={true}
                           placeholderTextColor='#B3B3B3'
                           selectionColor='#000000'
                           style={styles.textInput}
                ></TextInput>
            </View>
        );
    }
}


const styles = StyleSheet.create({
    container: {
        backgroundColor: '#FFFFFF',
        flex: 1
    },
    textInput: {
        fontSize: 14 * fontScale,
        margin: 10 * screenScale,
        flex: 1,
        color: '#B3B3B3'
    }
});
module.exports = IntroduceWorldScreen;