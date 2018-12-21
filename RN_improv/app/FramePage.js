/**
 * Created by flyye on 22/9/17.
 * @providesModule FramePage
 *
 */
'use strict';

import React, {Component} from 'react'
import {Provider} from 'react-redux'
import { createStore } from 'redux'
import DrawersNavigator from './navigation'
import reducer from './redux/reducers'

const store = createStore(reducer)


class FramePage extends Component {
    render() {
        return (
            <Provider store={store}>
                <DrawersNavigator/>
            </Provider>
        );
    }
};


module.exports = FramePage;
