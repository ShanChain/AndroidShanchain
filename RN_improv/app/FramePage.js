/**
 * Created by flyye on 22/9/17.
 * @providesModule FramePage
 *
 */
'use strict';

import React, {Component} from 'react'
import {Provider} from 'react-redux'
import { createStore } from 'redux'
import TabsNavigator from './navigation'
import reducer from './redux/reducers'

const store = createStore(reducer)


class FramePage extends Component {
    render() {
        return (
            <Provider store={store}>
                <TabsNavigator/>
            </Provider>
        );
    }
};


module.exports = FramePage;
