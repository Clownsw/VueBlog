pub mod config;
pub mod controller;
pub mod dao;
pub mod pojo;
pub mod util;

#[macro_use]
extern crate lazy_static;

#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {
        let result = 2 + 2;
        assert_eq!(result, 4);
    }
}
