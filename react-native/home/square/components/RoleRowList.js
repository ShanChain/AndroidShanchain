/**
 * Created by flyye on 21/9/17.
 */

'use strict';

import React, {Component} from 'react';
import {
  StyleSheet,
  Text,
  View,
  Buttom,
  Image,
  FlatList
} from 'react-native';

class RoleRowListItem extends React.PureComponent {
  _onPress = () => {
    this.props.onPressItem(this.props.id);
  };

  render() {
    return (<Image source={ImageBuilder.getImage('long_text')} {...this.props} onPress={this._onPress}/>)
  }
}

class RoleRowList extends React.PureComponent {
  state = {
    selected: (new Map() : Map<string, boolean>)
  };

  _keyExtractor = (item, index) => item.id;

  _onPressItem = (id : string) => {
    // updater functions are preferred for transactional updates
    this.setState((state) => {
      // copy the map rather than modifying state.
      const selected = new Map(state.selected);
      selected.set(id, !selected.get(id)); // toggle
      return {selected};
    });
  };

  _renderItem = ({item}) => (<RoleRowListItem id={item.id} onPressItem={this._onPressItem} selected={!!this.state.selected.get(item.id)} title={item.title}/>);

  render() {
    return (<FlatList data={this.props.data} extraData={this.state} keyExtractor={this._keyExtractor} renderItem={this._renderItem}/>);
  }
}

module.exports = RoleRowList;
