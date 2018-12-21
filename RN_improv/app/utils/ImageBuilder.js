/**
 * Created by flyye on 21/9/17.
 */

/**
 * @param imageUrl
 */
function getImage(imageUrl) {
    switch (imageUrl) {
        case 'search_btn':
            return require('../img/abs_home_btn_news_selected.png');
        case 'delete_btn':
            return require('../img/abs_icon_delete_default.png');
        case 'homePageBG':
            return require('../img/learn.png');
        case 'menu_btn':
            return require('../img/menu.png');
        case 'message_btn':
            return require('../img/message.png');
        case 'girl':
            return require('../img/girl.jpg');
        default:
            return;
    }
}

module.exports = {
    getImage
};
