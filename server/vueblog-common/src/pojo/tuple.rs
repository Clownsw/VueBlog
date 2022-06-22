use std::marker;

use serde::{Deserialize, Serialize};

#[derive(Debug, Default, Serialize, Deserialize, Clone)]
pub struct Tuple<T, K> {
    pub left: T,
    pub right: K,
    #[serde(skip)]
    _marker: marker::PhantomData<T>,
    #[serde(skip)]
    _marker2: marker::PhantomData<K>,
}
