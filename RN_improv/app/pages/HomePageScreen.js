/**
 * Created by flyye on 22/9/27.
 * @providesModule HomePageScreen
 */


'use strict';

import React, {Component} from 'react';
import {
    StyleSheet,
    Text,
    View,
    Image,
    TouchableWithoutFeedback,
    Dimensions
} from 'react-native';
import ImageBuilder from '../utils/ImageBuilder';

const {width, height, scale, fontScale} = Dimensions.get('window');
const screenScale = width / 375;

class HomePageScreen extends Component {
    constructor(props) {
        super(props);
    }

    componentWillMount() {

    }

    refreshData() {

    }

    render() {
        return (
            <View>
                <View style={styles.headerContainer}>
                    <TouchableWithoutFeedback onPress={() => {
                        this.props.navigation.navigate('DrawerToggle');
                    }}>
                        <View style={{
                            position: 'absolute',
                            left: 0,
                            height: 60 * screenScale,
                            width: 60 * screenScale,
                            justifyContent: 'center'
                        }}>
                            <Image style={styles.headerBack} source={ImageBuilder.getImage('menu_btn')}/>
                        </View>
                    </TouchableWithoutFeedback>
                    <TouchableWithoutFeedback onPress={()=>{
                        this.props.navigation.navigate('DrawerToggle');
                    }}>
                        <View style={{
                            position: 'absolute',
                            right: 0,
                            height: 60 * screenScale,
                            width: 60 * screenScale,
                            justifyContent: 'center'
                        }}>
                            <Image style={styles.headerBack} source={ImageBuilder.getImage('message_btn')}/>
                        </View>
                    </TouchableWithoutFeedback>
                </View>
                <View style={styles.middle}>
                    <Text style={styles.middleText1}>三国历史狂想</Text>
                    <Text style={styles.middleText2}>梦回三国，再战金戈铁马</Text>
                    <View style={styles.middleButtons}>
                        <View style={styles.middleButton}>
                            <Text style={styles.middleBtnText}>
                                历史
                            </Text>
                        </View>
                        <View style={styles.middleButton}>
                            <Text style={styles.middleBtnText}>
                                军事
                            </Text>
                        </View>
                    </View>
                </View>
                <View style={styles.footer}>
                    <Text style={styles.footerText}>
                        史书上的只言片语，不足以描绘出一个灿烂的三国，对那些热血的历史细节，你是否也有各种各样的设想和猜测？
                        史书上的只言片语，不足以描绘出一个灿烂的三国，对那些热血的历史细节，你是否也有各种各样的设想和猜测？
                        史书上的只言片语，不足以描绘出一个灿烂的三国，对那些热血的历史细节，你是否也有各种各样的设想和猜测？
                    </Text>
                    <TouchableWithoutFeedback onPress={() => {
                        this.props.navigation.navigate('DrawerToggle');
                    }}>
                        <View style={styles.footerButton}>
                            <Text style={styles.footerButtonText}>
                                愉快地走进这个世界
                            </Text>
                        </View>
                    </TouchableWithoutFeedback>
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    headerContainer: {
        marginTop:30*screenScale,
        width: 375 * screenScale,
        height: 40 * screenScale,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center'
    },
    headerText: {
        fontSize: 18 * fontScale,
        color: '#666666'
    },
    headerBack: {
        width: 25 * screenScale,
        height: 25 * screenScale,
        marginLeft: 15 * screenScale
    },
    middle: {
        width: width,
        height: 110 * screenScale,
        padding: 20 * screenScale
    },
    middleText1: {
        fontSize: 16 * fontScale,
        color: '#2E2E2E',
        marginBottom: 14 * screenScale,
    },
    middleText2: {
        fontSize: 12 * fontScale,
        color: '#B3B3B3',
        marginBottom: 14 * screenScale,
    },
    middleButtons: {
        flexDirection: 'row',
    },
    middleButton: {
        width: 50 * screenScale,
        height: 24 * screenScale,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#00AAF6',
        borderRadius: 20 * screenScale,
        marginRight: 10 * screenScale
    },
    middleBtnText: {
        fontSize: 12 * fontScale,
        color: '#FFFFFF'
    },
    footer: {
        padding: 20 * screenScale
    },
    footerText: {
        fontSize: 14 * fontScale,
        color: '#666666',
    },
    footerButton: {
        marginTop: 20 * screenScale,
        width: width - 40 * screenScale,
        height: 42 * screenScale,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#00AAF6',
        borderRadius: 20 * screenScale
    },
    footerButtonText: {
        fontSize: 14 * fontScale,
        color: '#FFFFFF'
    }

});
module.exports = HomePageScreen;
