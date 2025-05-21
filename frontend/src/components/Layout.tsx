import React from 'react';
import { Link, Outlet } from 'react-router-dom';
import { Home, ShoppingCart, User } from 'lucide-react';

const Layout = () => {
    return (
        <div className="min-h-screen flex flex-col bg-gray-100">
            <header className="bg-blue-600 text-white shadow-md">
                {/* Верхняя часть хедера */}
                <div className="container mx-auto flex justify-between items-center h-16 px-4">
                    <Link to="/" className="flex items-center space-x-2 font-bold text-2xl">
                        <Home className="h-6 w-6" />
                        <span>Online Store</span>
                    </Link>

                    <nav className="flex items-center space-x-6">
                        <Link to="/orders" className="flex items-center space-x-1 hover:text-blue-200">
                            <ShoppingCart className="h-5 w-5" />
                            <span className="text-lg">Заказы</span>
                        </Link>
                        <Link to="/account" className="flex items-center space-x-1 hover:text-blue-200">
                            <User className="h-5 w-5" />
                            <span className="text-lg">Аккаунты</span>
                        </Link>
                    </nav>
                </div>
            </header>

            <main className="container mx-auto flex-1 p-4 mt-4">
                <Outlet />
            </main>
        </div>
    );
};

export default Layout;