import React from 'react';
import {DrawerItems, DrawerNavigator, StackNavigator} from 'react-navigation';
import ImageBuilder from './utils/ImageBuilder';

import base from './pages/index';
import {
    StyleSheet,
    Dimensions,
    Image,
    ScrollView,
    View,
    ImageBackground,
    Text,
    TouchableWithoutFeedback
} from 'react-native';

const {width, height, scale, fontScale} = Dimensions.get('window');
const screenScale = width / 375;

const DrawersNavigator = DrawerNavigator(
    {
        Stroy: {
            screen: base.HomePageScreen
        }
    }, {
        contentComponent: props => {
            return (
                <ScrollView>
                    <View>
                        <View style={styles.header}>
                            <ImageBackground source={
                                ImageBuilder.getImage('homePageBG')
                            } style={{
                                flexDirection: 'column',
                                flex: 1,
                                alignItems: 'center',
                            }}>
                                <Image
                                    source={ImageBuilder.getImage('girl')}
                                    style={{
                                        width: 50 * screenScale,
                                        height: 50 * screenScale,
                                        borderRadius: 25 * screenScale,
                                        marginTop: 60 * screenScale
                                    }}
                                />
                                <Text style={styles.headerText}>千千社</Text>
                                <Text style={styles.headerText}>千千世界签约即兴表演团队</Text>
                            </ImageBackground>
                        </View>
                        <TouchableWithoutFeedback onPress={() => {
                            props.navigation.navigate('Stroy');
                        }}>
                            <View style={styles.itemContainer}>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                                <Text style={styles.leftText}>我的视频</Text>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                            </View>
                        </TouchableWithoutFeedback>
                        <View style={styles.girdWidenLine}/>
                        <TouchableWithoutFeedback onPress={() => {
                            props.navigation.navigate('SelectLabelsScreen');
                        }}>
                            <View style={styles.itemContainer}>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                                <Text style={styles.leftText}>我的素材</Text>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                            </View>
                        </TouchableWithoutFeedback>
                        <View style={styles.girdWidenLine}/>
                        <TouchableWithoutFeedback onPress={() => {
                            props.navigation.navigate('CopyRightLevel');
                        }}>
                            <View style={styles.itemContainer}>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                                <Text style={styles.leftText}>意见反馈</Text>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                            </View>
                        </TouchableWithoutFeedback>
                        <View style={styles.girdWidenLine}/>
                        <TouchableWithoutFeedback onPress={() => {
                            props.navigation.navigate('IntroduceWorld');
                        }}>
                            <View style={styles.itemContainer}>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                                <Text style={styles.leftText}>使用向导</Text>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                            </View>
                        </TouchableWithoutFeedback>
                        <View style={styles.girdWidenLine}/>
                        <TouchableWithoutFeedback onPress={() => {
                            props.navigation.navigate('NewWorld');
                        }}>
                            <View style={styles.itemContainer}>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                                <Text style={styles.leftText}>关于千千世界</Text>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                            </View>
                        </TouchableWithoutFeedback>
                        <View style={styles.girdWidenLine}/>
                        <TouchableWithoutFeedback onPress={() => {
                            props.navigation.navigate('NewWorld');
                        }}>
                            <View style={styles.itemContainer}>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                                <Text style={styles.leftText}>设置</Text>
                                <Image style={styles.icon}
                                       source={ImageBuilder.getImage('search_btn')}></Image>
                            </View>
                        </TouchableWithoutFeedback>
                        <View style={styles.girdWidenLine}/>
                    </View>
                </ScrollView>
            )
        }
    }
);

const AllNavigator = StackNavigator(
    {
        DrawersNavigator: {
            screen: DrawersNavigator,
            navigationOptions: ({navigation}) => ({
                header: null,
            }),
        },
        AllStackNavigatorZ: {screen: base.SelectLabelsScreen},
        CopyRightLevel: {screen: base.CopyRightLevelScreen},
        IntroduceWorld: {screen: base.IntroduceWorldScreen},
        NewWorld: {screen: base.NewWorldScreen}
    }, {
        initialRouteName: 'DrawersNavigator',
        headerMode: 'screen',
        navigationOptions: {
            headerStyle: {
                backgroundColor: '#3e9ce9',
            },
            headerTitleStyle: {
                color: '#fff',
                fontSize: 20
            },
            headerTintColor: '#fff'
        }
    }
)

const styles = StyleSheet.create({
    icon: {
        width: 25 * screenScale,
        height: 25 * screenScale
    },
    header: {
        flexDirection: 'column',
        backgroundColor: '#EEEEEE',
        height: 200 * screenScale,
    },
    headerText: {
        backgroundColor: 'rgba(0,0,0,0)',
        fontSize: 16 * screenScale,
        marginTop: 20 * screenScale
    },
    itemContainer: {
        flex: 1,
        flexDirection: 'row',
        backgroundColor: '#FFFFFF',
        alignItems: 'center',
        justifyContent: 'center',
        padding: 10 * screenScale
    },
    leftText: {
        marginLeft: 20 * screenScale,
        flex: 1
    },
    girdWidenLine: {
        height: 1,
        width: 375 * screenScale,
        backgroundColor: '#EEEEEE'
    }
})
module.exports = AllNavigator;