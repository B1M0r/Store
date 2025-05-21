import { useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Label } from '@/components/ui/label';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { DialogFooter } from '@/components/ui/dialog';
import { toast } from '@/hooks/use-toast';
import { createProduct } from '@/services/api';
import { Product } from '@/types';

interface ProductFormProps {
    product?: Product;
    onSuccess?: () => void;
}

const ProductForm = ({ product, onSuccess }: ProductFormProps) => {
    const queryClient = useQueryClient();
    const [loading, setLoading] = useState(false);
    const [form, setForm] = useState<Partial<Product>>({
        name: product?.name || '',
        category: product?.category || '',
        price: product?.price || 0,
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({
            ...prev,
            [name]: name === 'price' ? Number(value) : value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!form.name || !form.category || form.price === undefined) {
            toast({
                title: "Ошибка валидации",
                description: "Пожалуйста, заполните все поля",
                variant: "destructive",
            });
            return;
        }

        try {
            setLoading(true);
            await createProduct(form as Product);
            queryClient.invalidateQueries({ queryKey: ['products'] });
            toast({
                title: "Успех",
                description: "Продукт успешно создан",
            });
            if (onSuccess) onSuccess();
        } catch (error) {
            toast({
                title: "Ошибка",
                description: "Ошибка при создании продукт",
                variant: "destructive",
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4 py-4">
            <div className="space-y-2">
                <Label htmlFor="name">Название товара</Label>
                <Input
                    id="name"
                    name="name"
                    value={form.name}
                    onChange={handleChange}
                    placeholder="Введите название продукта"
                    required
                />
            </div>

            <div className="space-y-2">
                <Label htmlFor="category">Категория</Label>
                <Input
                    id="category"
                    name="category"
                    value={form.category}
                    onChange={handleChange}
                    placeholder="Введите категорию продукта"
                    required
                />
            </div>

            <div className="space-y-2">
                <Label htmlFor="price">Цена</Label>
                <Input
                    id="price"
                    name="price"
                    type="number"
                    value={form.price || ''}
                    onChange={handleChange}
                    placeholder="Введите цену продукта"
                    min="0"
                    step="1"
                    required
                />
            </div>

            <DialogFooter className="pt-4">
                <Button
                    type="submit"
                    disabled={loading}
                    className="bg-blue-600 hover:bg-blue-700 text-white w-full sm:w-auto"
                >
                    {loading ? 'Сохранение...' : 'Сохранить'}
                </Button>
            </DialogFooter>
        </form>
    );
};

export default ProductForm;