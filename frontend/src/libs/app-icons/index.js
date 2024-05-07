/* import the fontawesome core */
import {library} from '@fortawesome/fontawesome-svg-core';

/* import font awesome icon component */
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome';

/* import specific icons */
import {
    faBan,
    faHome,
    faUser,
    faBell,
    faEnvelope,
    faCog,
    faImage,
    faClock,
    faThumbsUp,
    faComment,
    faSync,
    faPencil,
    faEarth,
    faSortDown,
    faUserGroup,
    faLock,
    faUsers,
    faUserFriends,
    faUsersBetweenLines,
    faUsersLine,
    faUserPlus,
    faPersonCirclePlus,
    faPenToSquare,
    faPersonCircleXmark,
    faTriangleExclamation
} from "@fortawesome/free-solid-svg-icons";

/* add icons to the library */
library.add(faBan, faHome, faUser, faBell, faEnvelope, faCog, faImage, faClock, faThumbsUp, faComment, faSync, faPencil, faEarth, faSortDown, faUserGroup, faLock, faUsers, faUserFriends, faUsersBetweenLines, faUsersLine, faUserPlus, faPersonCirclePlus, faPenToSquare, faPersonCircleXmark, faTriangleExclamation);

export {FontAwesomeIcon};