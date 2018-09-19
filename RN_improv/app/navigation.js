import React from 'react';
import {DrawerNavigator, StackNavigator} from 'react-navigation';
import ImageBuilder from './utils/ImageBuilder';

import base from './pages/index';
import {
    StyleSheet,
    Dimensions,
    Image
} from 'react-native';

const {width, height, scale, fontScale} = Dimensions.get('window');
const screenScale = width / 375;

const DrawersNavigator = DrawerNavigator({
    Stroy: {
        screen: base.HomePageScreen,
        navigationOptions: {
            drawerLabel: '故事',
            drawerIcon: ({tintColor}) => (
                <Image
                    source={ImageBuilder.getImage('search_btn')}
                    style={[{tintColor: tintColor}, styles.icon]}
                />
            ),
        },
    },
    message: {
        screen: base.CopyRightLevelScreen,
        navigationOptions: {
            drawerLabel: '消息',
            drawerIcon: ({tintColor}) => (
                <Image
                    source={ImageBuilder.getImage('search_btn')}
                    style={[{tintColor: tintColor}, styles.icon]}
                />
            ),
        }
    },
    discover: {
        screen: base.IntroduceWorldScreen,
        navigationOptions: {
            drawerLabel: '发现',
            drawerIcon: ({tintColor}) => (
                <Image
                    source={ImageBuilder.getImage('search_btn')}
                    style={[{tintColor: tintColor}, styles.icon]}
                />
            ),
        }
    },
    mine: {
        screen: base.HomePageScreen,
        navigationOptions: {
            drawerLabel: '我的',
            drawerIcon: ({tintColor}) => (
                <Image
                    source={ImageBuilder.getImage('search_btn')}
                    style={[{tintColor: tintColor}, styles.icon]}
                />
            ),
        }
    }
});

const AllNavigator = StackNavigator(
    {
        DrawersNavigator: {screen: DrawersNavigator},
        CharacterRequire: {screen: base.CharacterRequireScreen},
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
    }
})
module.exports = AllNavigator;